package com.example.helloworld.db;

import com.example.helloworld.core.User;
import com.example.helloworld.mapper.UserMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Set;

@RegisterMapper(UserMapper.class)
public interface UserDAO {

    @SqlUpdate("insert into users (username, password, firstname, lastname, address) values (:username, :password, :firstname, :lastname, :address) RETURNING *")
    public User create(@Bind("username") String username, @Bind("password") String password, @Bind("firstname") String firstname, @Bind("lastname") String lastname, @Bind("address") String address);

    @SqlQuery("select username, password, firstname, lastname, address from users where username = :username")
    public User retrieve(@Bind("username") String username);

    @SqlUpdate("update users set password = :password, firstname = :firstname, lastname = :lastname, address = :address where username = :username")
    public void update(@Bind("username") String username, @Bind("password") String password, @Bind("firstname") String firstname, @Bind("lastname") String lastname, @Bind("address") String address);

    @SqlUpdate("delete from users where username = :username")
    public void delete(@BindBean User user);

    @SqlQuery("select username, password, firstname, lastname, address from users limit :limit offset :offset")
    public Set<User> list(@Bind("limit") int limit, @Bind("offset") int offset);
}


