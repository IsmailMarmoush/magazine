package io.memoria.magazine.domain.model.article;

import io.memoria.magazine.domain.model.MagazineError.InvalidArticleState;
import io.memoria.magazine.domain.model.MagazineError.UnauthorizedError;
import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.article.Tests.ALEX_JOURNALIST;
import static io.memoria.magazine.domain.model.article.Tests.BOB_JOURNALIST;
import static io.memoria.magazine.domain.model.article.Tests.BOB_OOP_ARTICLE;
import static io.memoria.magazine.domain.model.article.Tests.COMMAND_HANDLER;
import static io.memoria.magazine.domain.model.article.Tests.SUSAN_EDITOR;
import static org.assertj.core.api.Assertions.assertThat;

public class EditArticleTest {
  private static final String NEW_TITLE = "Object Oriented Design";

  @Test
  @DisplayName("Owner journalist should edit article successfully")
  public void editArticleTitle() {
    var editArticleCmd = new EditArticleTitle(BOB_JOURNALIST, BOB_OOP_ARTICLE.id(), NEW_TITLE);
    var events = COMMAND_HANDLER.apply(BOB_OOP_ARTICLE, editArticleCmd);
    assertThat(events.isSuccess()).isTrue();
    assertThat(events.get().contains(new ArticleTitleEdited(BOB_OOP_ARTICLE.id(), NEW_TITLE)));
  }

  @Test
  @DisplayName("Non journalist editing article should throw unauthorized exception")
  public void journalist() {
    var editArticleCmd = new EditArticleTitle(SUSAN_EDITOR, BOB_OOP_ARTICLE.id(), NEW_TITLE);
    var events = COMMAND_HANDLER.apply(BOB_OOP_ARTICLE, editArticleCmd);
    assertThat(events.isFailure()).isTrue();
    assertThat(events.getCause()).isExactlyInstanceOf(UnauthorizedError.class);
  }

  @Test
  @DisplayName("Non owner editing article should throw unauthorized exception")
  public void ownersOnly() {
    var editArticleCmd = new EditArticleTitle(ALEX_JOURNALIST, BOB_OOP_ARTICLE.id(), NEW_TITLE);
    var events = COMMAND_HANDLER.apply(BOB_OOP_ARTICLE, editArticleCmd);
    assertThat(events.isFailure()).isTrue();
    assertThat(events.getCause()).isExactlyInstanceOf(UnauthorizedError.class);
  }

  @Test
  @DisplayName("Article should not be empty")
  public void notEmpty() {
    var editArticleCmd = new EditArticleTitle(BOB_JOURNALIST, BOB_OOP_ARTICLE.id(), NEW_TITLE);
    var events = COMMAND_HANDLER.apply(Article.empty(), editArticleCmd);
    assertThat(events.isFailure()).isTrue();
    assertThat(events.getCause()).isEqualTo(InvalidArticleState.EMPTY_ARTICLE);
  }
}