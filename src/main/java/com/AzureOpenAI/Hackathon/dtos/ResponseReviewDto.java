package com.AzureOpenAI.Hackathon.dtos;

//public class ResponseReviewDto {
//}

public record ResponseReviewDto (
        String review,
        String goodnessQuotent,
        String nextQuestionDifficulty
){}
