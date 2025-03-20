package work.dduo.ans.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import work.dduo.ans.model.vo.response.GetAllResp;

// 该接口提供了基本的CRUD操作。
public interface GetAllRespRepository extends ElasticsearchRepository<GetAllResp, String> {

}
