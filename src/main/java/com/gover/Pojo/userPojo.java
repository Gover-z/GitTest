package com.gover.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gover
 * @verson 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class userPojo {
    private String userId;
    private String userName;
    private double userAllBalance;
}
