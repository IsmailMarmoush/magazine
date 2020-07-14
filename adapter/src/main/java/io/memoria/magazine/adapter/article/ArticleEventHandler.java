package io.memoria.magazine.adapter.article;

import io.memoria.jutils.eventsourcing.event.EventHandler;
import io.memoria.magazine.core.domain.Article;
import io.memoria.magazine.core.domain.ArticleStatus;
import io.memoria.magazine.core.services.dto.ArticleEvent;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleCreated;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleTitleEdited;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticlePublished;

public class ArticleEventHandler implements EventHandler<Article, ArticleEvent> {
  @Override
  public Article apply(Article original, ArticleEvent articleEvent) {
    if (articleEvent instanceof ArticleCreated ac) {
      return new Article(ac.title(), ac.content(), ArticleStatus.DRAFT);
    } else if (articleEvent instanceof ArticleTitleEdited ac) {
      return original.withTitle(ac.newTitle());
    } else if (articleEvent instanceof ArticlePublished) {
      return original.withPublished();
    }
    throw new UnsupportedOperationException();
  }
}
