package com.nva.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entrance_score_information")
public class EntranceScoreInformation extends Information implements Serializable {
    @ManyToOne
    @JoinColumn(name = "entrance_method_id", nullable = false)
    private EntranceMethod entranceMethod;
    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;
    @ManyToOne
    @JoinColumn(name = "year_id", nullable = false)
    private Year year;
}
