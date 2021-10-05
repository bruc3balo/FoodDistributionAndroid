package com.victoria.fooddistribution.domain;

import static com.victoria.fooddistribution.globals.GlobalVariables.USER_COLLECTION;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

public class Domain {

    @Entity(tableName = USER_COLLECTION)
    public static class AppUser {

        @PrimaryKey
        @NotNull
        private String uid;
        private String name;
        private String username;
        private String email_address;
        private String password;
        private String created_at;
        private String updated_at;
        private Boolean is_deleted;
        private Boolean is_disabled;
        private String role;

        public AppUser() {

        }

        public AppUser(@NonNull String uid, String username) {
            this.uid = uid;
            this.username = username;
        }

        public AppUser(@NonNull String uid) {
            this.uid = uid;
        }

        public AppUser(String name, String username, String email_address, String password) {
            this.name = name;
            this.username = username;
            this.email_address = email_address;
            this.password = password;
        }

        public AppUser(String name, String username, String email_address, String password, Boolean is_deleted, Boolean is_disabled, String role) {
            this.name = name;
            this.username = username;
            this.email_address = email_address;
            this.password = password;
            this.role = role;
            this.is_disabled = is_disabled;
            this.is_deleted = is_deleted;
        }

        public AppUser(String name, String username, String email_address, String password, String created_at, String updated_at, Boolean is_deleted, Boolean is_disabled, String role) {

            this.name = name;
            this.username = username;
            this.email_address = email_address;
            this.password = password;
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.is_deleted = is_deleted;
            this.is_disabled = is_disabled;
            this.role = role;
        }

        @NonNull
        public String getUid() {
            return uid;
        }

        public void setUid(@NonNull String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail_address() {
            return email_address;
        }

        public void setEmail_address(String email_address) {
            this.email_address = email_address;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public Boolean getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(Boolean is_deleted) {
            this.is_deleted = is_deleted;
        }

        public Boolean getIs_disabled() {
            return is_disabled;
        }

        public void setIs_disabled(Boolean is_disabled) {
            this.is_disabled = is_disabled;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }


}
