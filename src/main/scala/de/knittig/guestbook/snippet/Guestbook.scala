package de.knittig.guestbook.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import de.knittig.guestbook.model._

class Guestbook {
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
    Post.findAll.flatMap(post =>
      bind("post", chooseTemplate("guestbook", "post", xhtml),
          "name" -> <a href={"mailto:"+post.email}>{post.name}</a>,
          "text" -> post.text))
  }
}

