package com.evan.wj.service;

import com.evan.wj.dao.JotterArticleDAO;
import com.evan.wj.pojo.JotterArticle;
import com.evan.wj.redis.RedisService;
import com.evan.wj.utils.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class JotterArticleService {
    @Autowired
    JotterArticleDAO jotterArticleDAO;
    @Autowired
    RedisService redisService;

    public void addOrUpdate(JotterArticle article) {
        jotterArticleDAO.save(article);

        // 删除当前选中的文章和所有文章页面的缓存
        redisService.delete("article" + article.getId());
        Set<String> keys = redisService.getKeysByPattern("articlepage*");
        redisService.delete(keys);
    }


    public MyPage list(int page, int size) {
        MyPage<JotterArticle> articles;
        String key = "articlepage:" + page;
        Object articlePageCache = redisService.get(key);

        if (articlePageCache == null) {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            Page<JotterArticle> articlesInDb = jotterArticleDAO.findAll(PageRequest.of(page, size, sort));
            articles = new MyPage<>(articlesInDb);
            redisService.set(key, articles);
        }else {
            articles = (MyPage<JotterArticle>) articlePageCache;
        }

        return  articles;
    }

    public void delete(int id) {
        jotterArticleDAO.deleteById(id);

        redisService.delete("article" + id);
        Set<String> keys = redisService.getKeysByPattern("articlepage*");
        redisService.delete(keys);

    }


    public JotterArticle findById(int id) {
        JotterArticle article;
        // 用户访问具体文章时缓存单篇文章，通过 id 区分
        String key = "article:" + id;
        Object articleCache = redisService.get(key);
        if (articleCache == null) {
            article =  jotterArticleDAO.findById(id);
            redisService.set(key,article);
        }else {
            article = (JotterArticle) articleCache;
        }
        return article;
    }
}
