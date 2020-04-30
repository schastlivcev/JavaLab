package ru.kpfu.itis.rodsher.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendsId implements Serializable {
    private User userSender;
    private User userRecipient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendsId friendsId = (FriendsId) o;
        return userSender.equals(friendsId.userSender) &&
                userRecipient.equals(friendsId.userRecipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userSender, userRecipient);
    }
}
