package webdata;

import java.util.ArrayList;

public class ReviewData {
    private String[] productId;
    private String[] reviewHelpfulness;
    private String[] reviewScore;
    private String[] tokensPerReview;
    private int numOfReviews;

    ReviewData (ArrayList<String> productId, ArrayList<String> reviewHelpfulness, ArrayList<String> reviewScore,
                ArrayList<String> tokensPerReview, int numOfReviews) {
        this.productId = new String[numOfReviews];
        this.productId = productId.toArray(this.productId);
        this.reviewHelpfulness = new String[numOfReviews];
        this.reviewHelpfulness = reviewHelpfulness.toArray(this.reviewHelpfulness);
        this.reviewScore = new String[numOfReviews];
        this.reviewScore = reviewScore.toArray(this.reviewScore);
        this.tokensPerReview = new String[numOfReviews];
        this.tokensPerReview = tokensPerReview.toArray(this.tokensPerReview);
        this.numOfReviews = numOfReviews;
    }

    public String getReviewScore(int i) {
        return reviewScore[i];
    }

    public String getReviewHelpfulness(int i) {
        return reviewHelpfulness[i];
    }

    public String getProductId(int i) {
        return productId[i];
    }

    public String getTokensPerReview(int i) {
        return tokensPerReview[i];
    }

    public int getNumOfReviews() {
        return numOfReviews;
    }
}
