package uz.md.shopapp.domain.template;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class AbsTimeStampEntity {

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime addedAt;

    @UpdateTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime updatedAt;

    private boolean deleted;

    private boolean active;
}
