package com.donny.community.service;

import com.donny.community.entity.DiscussPost;
import org.springframework.data.domain.Page;

public interface ElasticSearchService {

    void saveDiscussPost(DiscussPost post);

    void deleteDiscussPost(Integer id);

    Page<DiscussPost> searchDiscussPost(String keyword, Integer current, Integer limit);

}