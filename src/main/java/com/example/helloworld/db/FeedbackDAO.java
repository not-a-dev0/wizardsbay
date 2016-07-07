package com.example.helloworld.db;

import com.example.helloworld.core.Feedback;
import com.example.helloworld.core.User;
import com.example.helloworld.mapper.*;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.Set;

/**
 * Created by susannaedens on 6/20/16.
 */

@RegisterMapper(FeedbackMapper.class)
public interface FeedbackDAO {

    @SqlUpdate("insert into feedback (userid, feedbackdesc) values (:userid, :feedbackdesc)")
    public void create(@Bind("userid") int userid, @Bind("feedbackdesc") String feedbackdesc);

    @SqlQuery("select id, userid, feedbackdesc from feedback where id = :id")
    public Feedback retrieveOne(@Bind("id") int id);

    @SqlQuery("select * from feedback where userid = :userid")
    public List<Feedback> retrieve(@Bind("userid") int userid);

    //> Delete feedback by unique id
    @SqlUpdate("delete from feedback where id = :id")
    public void delete(@BindBean Feedback feedback);
    

}
