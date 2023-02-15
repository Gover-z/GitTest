package com.gover.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Gover
 * @verson 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class balancePojo {
    private String userId;
    private String balanceId;
    private double balanceBil;
    private String balanceType;
    private Timestamp balanceTime;
}
