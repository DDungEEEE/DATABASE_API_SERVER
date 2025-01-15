package net.ddns.sbapiserver.repository.common;
import net.ddns.sbapiserver.domain.entity.client.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
