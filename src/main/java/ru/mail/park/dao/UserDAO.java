package ru.mail.park.dao;


import ru.mail.park.exceptions.DatabaseConnectionException;
import ru.mail.park.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(UserDAO.class.getName());

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean register(@NotNull User userToRegister) {
        try {
            final String sql = "INSERT INTO \"User\" (email, nickname, password) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, userToRegister.getEmail(), userToRegister.getNickname(), userToRegister.getPassword());
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public Boolean login(@NotNull User userToLogin) {
        try {
            final String sql = "SELECT * FROM \"User\" WHERE email = ?";
            final User user = jdbcTemplate.queryForObject(sql, new Object[]{userToLogin.getEmail()}, new UserMapper());

            if (user.getPassword().equals(userToLogin.getPassword())) {
                return true;
            }
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            throw new DatabaseConnectionException("Can't connect to the database", e);
        }


        return false;
    }

    public User getUser(@NotNull String email) {
        try {
            final String sql = "SELECT * FROM \"User\" WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, new UserMapper());
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            throw new DatabaseConnectionException("Can't connect to the database", e);
        }
    }

    @SuppressWarnings({"OverlyComplexMethod", "SingleCharacterStringConcatenation"})
    public Boolean updateUser(@NotNull User userToUpdate) {
        final Boolean hasPassword = userToUpdate.getPassword() != null && !userToUpdate.getPassword().isEmpty();
        final Boolean hasNickname = userToUpdate.getNickname() != null && !userToUpdate.getNickname().isEmpty();
        final Boolean hasRating = userToUpdate.getRating() != null && userToUpdate.getRating() >= 0;
        final Boolean condition = hasNickname || hasPassword || hasRating;

        if (condition) {
            final List<Object> sqlParameters = new ArrayList<>();
            final StringBuilder sql = new StringBuilder("UPDATE \"User\" SET");

            if (hasNickname) {
                sql.append(" nickname = ?");
                sqlParameters.add(userToUpdate.getNickname());
            }

            if (hasPassword) {
                if (hasNickname) {
                    sql.append(",");
                }
                sql.append(" password = ?");
                sqlParameters.add(userToUpdate.getPassword());
            }

            if (hasRating) {
                if (hasNickname || hasPassword) {
                    sql.append(",");
                }
                sql.append(" rating = ?");
                sqlParameters.add(userToUpdate.getRating());
            }

            sql.append(" WHERE email = ?;");
            sqlParameters.add(userToUpdate.getEmail());

            try {
                jdbcTemplate.update(sql.toString(), sqlParameters.toArray());
                return true;
            } catch (DataAccessException e) {
                logger.log(Level.WARNING, "Exception : ", e);
                throw new DatabaseConnectionException("Can't connect to the database", e);
            }
        }

        return false;
    }

    @SuppressWarnings("RedundantArrayCreation")
    public Boolean updateAvatar(@NotNull User userToUpdate) {
        final Boolean hasAvatarLink = userToUpdate.getAvatar() != null && !userToUpdate.getAvatar().isEmpty();

        if (hasAvatarLink) {
            try {
                jdbcTemplate.update("UPDATE \"User\" SET avatar = ? WHERE email = ?;",
                        new Object[]{userToUpdate.getAvatar(), userToUpdate.getEmail()});
                return true;
            } catch (DataAccessException e) {
                logger.log(Level.WARNING, "Exception : ", e);
                throw new DatabaseConnectionException("Can't connect to the database", e);
            }
        }

        return false;
    }

    public List<User> getSortedUsersInfoByRating(int userPerPage, int page) {
        try {
            final int offset = (page - 1) * userPerPage;
            final String sql = "SELECT * FROM \"User\" ORDER BY rating DESC LIMIT ? OFFSET ?;";
            return jdbcTemplate.query(sql, new Object[]{userPerPage, offset}, new UserInfoMapper());
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            throw new DatabaseConnectionException("Can't connect to the database", e);
        }
    }

    public int getLastPage(int userPerPage) {
        try {
            final int numberOfUsers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM \"User\";", Integer.class);
            return numberOfUsers / userPerPage + 1;
        } catch (DataAccessException e) {
            return 0;
        }
    }


    public static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int incParam) throws SQLException {
            final User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setEmail(resultSet.getString("email"));
            user.setNickname(resultSet.getString("nickname"));
            user.setPassword(resultSet.getString("password"));
            user.setRating(resultSet.getInt("rating"));
            user.setAvatar(resultSet.getString("avatar"));

            return user;
        }
    }

    public static class UserInfoMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int incParam) throws SQLException {
            final User user = new User();
            user.setNickname(resultSet.getString("nickname"));
            user.setRating(resultSet.getInt("rating"));
            user.setAvatar(resultSet.getString("avatar"));

            return user;
        }
    }
}
