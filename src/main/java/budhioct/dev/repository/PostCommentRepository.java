package budhioct.dev.repository;

import budhioct.dev.entity.PostCommentEntity;
import budhioct.dev.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostCommentEntity, Long>, JpaSpecificationExecutor<PostCommentEntity> {

    Optional<PostCommentEntity> findFirstByPostsAndId(PostEntity posts, long id);

}
