package com.wisdom.blog.service;

import com.wisdom.blog.domain.Vote;

public interface VoteService {

    Vote getVoteById(Long id);

    void removeVote(Long id);

}
