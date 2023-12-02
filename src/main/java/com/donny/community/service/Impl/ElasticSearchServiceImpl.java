package com.donny.community.service.Impl;

import com.donny.community.dao.elasticsearch.DiscussPostRepository;
import com.donny.community.entity.DiscussPost;
import com.donny.community.service.ElasticSearchService;
import lombok.Setter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchRestTemplate template;

    @Override
    public void saveDiscussPost(DiscussPost post) {
        discussPostRepository.save(post);
    }

    @Override
    public void deleteDiscussPost(Integer id) {
        discussPostRepository.deleteById(id);
    }

    @Override
    public Page<DiscussPost> searchDiscussPost(String keyword, Integer current, Integer limit) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        SearchHits<DiscussPost> searchHits = template.search(searchQuery, DiscussPost.class);
        if (searchHits.getTotalHits() <= 0) return Page.empty();
        List<SearchHit<DiscussPost>> hits = searchHits.getSearchHits();

        List<DiscussPost> list = new ArrayList<>();
        for (SearchHit<DiscussPost> hit : hits) {
            // 获取匹配的post
            DiscussPost post = hit.getContent();
            // 设置高亮
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            post.setTitle(highlightFields.get("title") == null ? post.getTitle() : highlightFields.get("title").get(0));
            post.setContent(highlightFields.get("content") == null ? post.getContent() : highlightFields.get("content").get(0));
            list.add(post);
        }
//         手动构造分页对象Page
        Pageable pageable = PageRequest.of(current, limit);
        Page<DiscussPost> discussPosts = new PageImpl<>(list, pageable, searchHits.getTotalHits());

        return discussPosts;
    }
}
