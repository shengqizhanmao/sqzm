package com.lin.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Author;
import org.springframework.transaction.TransactionStatus;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-22
 */
public interface AuthorService extends IService<Author> {

    Result isAuthor(String token);

    Result apply(String token);

    Result add(String userId, String status);

    Result getAuthorVoList(String status, Long size, Long page);

    boolean deleteByUserId(String id, TransactionStatus transaction);
}
