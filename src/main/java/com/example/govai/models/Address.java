package com.example.govai.models;

import com.example.govai.commons.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
@SQLDelete(sql = "UPDATE addresses SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Address extends BaseEntity {

    @Column(length = 8, name = "cep", nullable = false)
    private String cep;

    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;

    @Column(length = 2)
    private String state;

    @Column(length = 80, nullable = false, columnDefinition = "VARCHAR(80) DEFAULT 'Brasil'")
    private String country = "Brasil";
}
