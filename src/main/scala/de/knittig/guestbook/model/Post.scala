package de.knittig.guestbook.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util.FieldError
import scala.xml.Text
import net.liftweb.http.S

object Post extends Post with LongKeyedMetaMapper[Post] {
	
}

class Post extends LongKeyedMapper[Post] with IdPK {
  def getSingleton = Post
  
  object name extends MappedString(this, 255) {
    override def validations = valMinLen(3, "Name must be 3 characters long") _ :: super.validations
  }
  object email extends MappedBlankableEmail(this, 255)
  
  object text extends MappedTextarea(this, 8192) {
    override def textareaRows  = 6
    override def textareaCols = 100
    
    override def validations = valMinLen(10, "Text must be 10 characters long") _ :: super.validations
  }
}

class MappedBlankableEmail[T<:Mapper[T]](owner: T, maxLen: Int) extends MappedString[T](owner, maxLen) {
  override def setFilter = notNull _ :: toLower _ :: trim _ :: super.setFilter

  override def validate =
    (if (i_is_!.length == 0 || MappedEmail.emailPattern.matcher(i_is_!).matches) Nil else List(FieldError(this, Text(S.??("invalid.email.address"))))) ::: super.validate
}