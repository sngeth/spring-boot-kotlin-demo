package com.example.demo

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

@Controller
class HtmlController(private val repository: ArticleRepository) {

  @GetMapping("/")
  fun blog(model: Model): String {
    model.addAttribute("title", "Blog")
    model.addAttribute("articles", repository.findAllByOrderByAddedAtDesc().map { it.render() })
    return "blog"
  }

  @GetMapping("/article/{slug}")
  fun article(@PathVariable slug: String, model: Model): String {
    val article = repository
        .findBySlug(slug)
        ?.render()
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This article does not exist")
    model.addAttribute("title", article.title)
    model.addAttribute("article", article)
    return "article"
  }

  fun Article.render() = RenderedArticle(
      slug,
      title,
      headline,
      content,
      author,
      addedAt.format()
  )

  data class RenderedArticle(
      val slug: String,
      val title: String,
      val headline: String,
      val content: String,
      val author: User,
      val addedAt: String)

}
