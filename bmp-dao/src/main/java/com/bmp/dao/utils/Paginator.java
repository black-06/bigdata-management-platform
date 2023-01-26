package com.bmp.dao.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.util.List;

/**
 * request paginator param
 */
@Data
public class Paginator {
    private int size;
    private int num;
    private List<OrderItem> orders;
}
