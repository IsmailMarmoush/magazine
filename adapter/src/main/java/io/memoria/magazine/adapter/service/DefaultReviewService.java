package io.memoria.magazine.adapter.service;

import io.memoria.jutils.eventsourcing.event.EventHandler;
import io.memoria.magazine.adapter.repo.EventRepo;
import io.memoria.magazine.domain.model.review.Review;
import io.memoria.magazine.domain.services.ReviewService;
import io.memoria.magazine.domain.model.review.ReviewCmd.CreateContentReview;
import io.memoria.magazine.domain.model.review.ReviewEvent;
import io.memoria.magazine.domain.model.review.ReviewEvent.ContentReviewCreated;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewFulfilled;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewResolved;
import reactor.core.publisher.Mono;

public record DefaultReviewService(EventRepo<ReviewEvent>repo,
                                   EventHandler<Review, ReviewEvent>eventHandler)
        implements ReviewService {
  @Override
  public Mono<ContentReviewCreated> create(String id, CreateContentReview s) {
    return repo.exists(id)
               .flatMap(exists -> (exists) ? Mono.error(new AlreadyExists()) : Mono.just(createReview(id, s)));
  }

  @Override
  public Mono<ReviewFulfilled> fulfill(String reviewId) {
    return repo.stream(reviewId).reduce(Review.empty(), eventHandler).map(review -> new ReviewFulfilled(reviewId));
  }

  @Override
  public Mono<ReviewResolved> resolve(String reviewId) {
    return repo.stream(reviewId).reduce(Review.empty(), eventHandler).map(review -> new ReviewResolved(reviewId));
  }

  private ContentReviewCreated createReview(String id, CreateContentReview s) {
    return new ContentReviewCreated(id, s.articleId(), s.newContent());
  }
}
