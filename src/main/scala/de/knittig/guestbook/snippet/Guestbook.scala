package de.knittig.guestbook.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import de.knittig.guestbook.model._
import net.liftweb.mapper._

class Guestbook {
  val defaultOffset = 2
  
  def addPost(form: NodeSeq) = {
    var post = new Post
    
    def save(): Unit = {
      post.validate match {
        case Nil => {
          if (post.save) {
            notice("Added post")
            redirectTo("/")
          } else {
            error("Failed to save post")
          }
         }
       case errors => {
         errors.foreach(err => error(err.msg))
         mapSnippet("Guestbook.addPost", doBind) 
       }
      }
    }

    def doBind(form: NodeSeq) =
      bind("post", form,
          "name" -> text(post.name, post.name(_)) % ("id" -> "name") % ("class" -> "text"),
          "email" -> text(post.email, post.email(_)) % ("id" -> "email") % ("class" -> "text"),
          "text" -> textarea(post.text, post.text(_)) % ("id" -> "text") % ("style" -> "height: 100px; width: 100%;"),
          "submit" -> submit("Post", save))
          
    doBind(form)
  }
  
  def posts(xhtml: NodeSeq) = {
    Post.findAll(StartAt(offset), MaxRows(defaultOffset)).flatMap(post =>
      bind("post", chooseTemplate("guestbook", "post", xhtml),
          "name" -> post.email.toLink(post.name),
          "text" -> post.text))
  }
  
  def pagination = <span>{firstPage} {prevPage} | {pages} | {nextPage} {lastPage}</span>
  def firstPage = if (hasPrevPage) <a href="?offset=0">&lt;&lt;</a> else <span>&lt;&lt;</span>
  def prevPage = if (hasPrevPage) <a href={"?offset=" + (offset - defaultOffset).toString}>&lt;</a> else <span>&lt;</span>
  def hasPrevPage = Post.count > offset && 0 <= (offset - defaultOffset)
  def lastPage = if (hasNextPage) <a href={"?offset=" + ((pageSize - 1) * defaultOffset).toString}>&gt;&gt;</a> else <span>&gt;&gt;</span>
  def nextPage = if (hasNextPage) <a href={"?offset=" + (offset + defaultOffset).toString}>&gt;</a> else <span>&gt;</span>
  def hasNextPage = Post.count > offset && Post.count > (offset + defaultOffset)
  def pages = {
    <span>
    {currentPageRange.map(page => <span> <a href={"?offset=" + toOffset(page)}>{page}</a> </span>)}
    </span>
  }
  def offset = param("offset").map(_.toLong) openOr 0L
  def toOffset(page: Long) = (page - 1) * defaultOffset
  def currentPageRange = (currentPage - 3).max(2).toInt until (currentPage + 3).min(pageSize).toInt
  def currentPage = (offset / defaultOffset) + 1
  def pageSize = ((Post.count - 1) / defaultOffset) + 1
}

