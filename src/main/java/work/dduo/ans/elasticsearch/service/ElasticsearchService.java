package work.dduo.ans.elasticsearch.service;

import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import work.dduo.ans.elasticsearch.repository.GetAllRespRepository;
import work.dduo.ans.model.vo.response.GetAllResp;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticsearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @Autowired
    private GetAllRespRepository getAllRespRepository ;

    /**
     * 保存数据到elasticsearch
     * @param getAllResp
     * @return
     */
    public List<GetAllResp> saveProduct( List<GetAllResp> getAllResp) {
//        这只是写单个数据
//        return (List<GetAllResp>) getAllRespRepository.save(getAllResp);
        return (List<GetAllResp>) getAllRespRepository.saveAll(getAllResp);
    }

    /**
     * 单字符串全文查询，支持分页和排序 查询包括字符串的所有数据 所有字段
     * @param queryString 查询字符串
     * @param page 页码
     * @param size 每页数量
     * @return 查询结果列表
     */
    public List<GetAllResp> fullTextSearch(String queryString, int page, int size) {
        // 构建查询条件
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(queryString);
        // 构建搜索查询，设置分页和排序
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page,  size))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC)) // 按照id排序 正序
                .build();
        // 执行查询
        SearchHits<GetAllResp> searchHits = elasticsearchTemplate.search(searchQuery,  GetAllResp.class);
        // 提取查询结果
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * 某字段按字符串模糊查询 只查询指定字段
     * @param field 字段名
     * @param value 查询值
     * @param page 页码
     * @param size 每页数量
     * @return 查询结果列表
     */
    public List<GetAllResp> fuzzySearchByField(String field, String value, int page, int size) {
        // 构建模糊查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(field,  value);
        // 构建搜索查询，设置分页
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page,  size))
                .build();
        // 执行查询
        SearchHits<GetAllResp> searchHits = elasticsearchTemplate.search(searchQuery,  GetAllResp.class);
        // 提取查询结果
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * PhraseMatch 查询（短语匹配）
     * @param field 字段名
     * @param phrase 短语
     * @param slop 允许的最大间隔
     * @param page 页码
     * @param size 每页数量
     * @return 查询结果列表
     */
    public List<GetAllResp> phraseMatchSearch(String field, String phrase, int slop, int page, int size) {
        // 构建短语匹配查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery(field,  phrase).slop(slop);
        // 构建搜索查询，设置分页
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page,  size))
                .build();
        // 执行查询
        SearchHits<GetAllResp> searchHits = elasticsearchTemplate.search(searchQuery,  GetAllResp.class);
        // 提取查询结果
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * Term 查询（精确查询）
     * @param field 字段名
     * @param value 查询值
     * @param page 页码
     * @param size 每页数量
     * @return 查询结果列表
     */
    public List<GetAllResp> termSearch(String field, Object value, int page, int size) {
        // 构建精确查询条件
        QueryBuilder queryBuilder = QueryBuilders.termQuery(field,  value);
        // 构建搜索查询，设置分页
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page,  size))
                .build();
        // 执行查询
        SearchHits<GetAllResp> searchHits = elasticsearchTemplate.search(searchQuery,  GetAllResp.class);
        // 提取查询结果
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * multi_match 多个字段匹配某字符串
     * @param fields 字段数组
     * @param value 查询值
     * @param page 页码
     * @param size 每页数量
     * @return 查询结果列表
     */
    public List<GetAllResp> multiMatchSearch(String[] fields, String value, int page, int size) {
        // 构建多字段匹配查询条件
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(value,  fields);
        // 构建搜索查询，设置分页
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page,  size))
                .build();
        // 执行查询
        SearchHits<GetAllResp> searchHits = elasticsearchTemplate.search(searchQuery,  GetAllResp.class);
        // 提取查询结果
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * 完全包含查询
     * @param field 字段名
     * @param value 查询值
     * @param operator 操作符（如 AND、OR）
     * @param minimumShouldMatch 最少匹配百分比
     * @param page 页码
     * @param size 每页数量
     * @return 查询结果列表
     */
    public List<GetAllResp> exactMatchSearch(String field, String value, String operator, String minimumShouldMatch, int page, int size) {
        // 构建匹配查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(field,  value)
                .operator(Operator.fromString(operator))
                .minimumShouldMatch(minimumShouldMatch);
        // 构建搜索查询，设置分页
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page,  size))
                .build();
        // 执行查询
        SearchHits<GetAllResp> searchHits = elasticsearchTemplate.search(searchQuery,  GetAllResp.class);
        // 提取查询结果
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * 合并查询（boolQuery）
     * @param mustField 必须匹配的字段
     * @param mustValue 必须匹配的值
     * @param filterField 过滤字段
     * @param filterValue 过滤值
     * @param shouldField 可选匹配的字段
     * @param shouldValues 可选匹配的值数组
     * @param page 页码
     * @param size 每页数量
     * @return 查询结果列表
     */
    public List<GetAllResp> boolQuerySearch(String mustField, String mustValue, String filterField, Object filterValue, String shouldField, List<Object> shouldValues, int page, int size) {
        // 构建布尔查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 添加 must 条件
        boolQuery.must(QueryBuilders.matchQuery(mustField,  mustValue));
        // 添加 filter 条件
        boolQuery.filter(QueryBuilders.termQuery(filterField,  filterValue));
        // 添加 should 条件
        for (Object shouldValue : shouldValues) {
            boolQuery.should(QueryBuilders.termQuery(shouldField,  shouldValue));
        }
        // 构建搜索查询，设置分页
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(page,  size))
                .build();
        // 执行查询
        SearchHits<GetAllResp> searchHits = elasticsearchTemplate.search(searchQuery,  GetAllResp.class);
        // 提取查询结果
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

}