package io.memoria.magazine.adapter;

import io.memoria.magazine.core.services.dto.ReviewCmd.CreateContentReview;
import io.memoria.magazine.core.services.dto.ReviewEvent.ContentReviewCreated;
import io.memoria.magazine.core.services.dto.ReviewEvent.ReviewFulfilled;
import io.memoria.magazine.core.services.dto.ReviewEvent.ReviewResolved;

import static io.memoria.magazine.adapter.ArticleTestData.ARTICLE_ID;

public class ReviewTestData {
  static final String REVIEW_ID = "review_01";
  static final String SUGGESTED_CONTENT = "Reactive Programming CS 101";
  static final ContentReviewCreated CONTENT_REVIEW_CREATED = new ContentReviewCreated(REVIEW_ID,
                                                                                      ARTICLE_ID,
                                                                                      SUGGESTED_CONTENT);
  static final ReviewFulfilled REVIEW_FULFILLED = new ReviewFulfilled(REVIEW_ID);
  static final ReviewResolved REVIEW_RESOLVED = new ReviewResolved(REVIEW_ID);

  static final CreateContentReview CREATE_CONTENT_REVIEW = new CreateContentReview(ARTICLE_ID, SUGGESTED_CONTENT);

  private ReviewTestData() {}
}