package budhioct.dev.repository;

import budhioct.dev.entity.PostCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostCommentRepository extends JpaRepository<PostCommentEntity, Long>, JpaSpecificationExecutor<PostCommentEntity> {

}
