//package com.ssg.itbody.dto;
//
//import com.ssg.itbody.entity.UserEntity;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@RequiredArgsConstructor
//public class CustomuserDetails implements UserDetails {
//
//    private final UserEntity userEntity;
//
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        Collection<GrantedAuthority> collection = new ArrayList<>();
////        collection.add(new GrantedAuthority() {
////            @Override
////            public String getAuthority() {
////                return userEntity.getMembershipGrade().getMembershipGrade();
////            }
////        });
////        return collection;
////    }
//
//    @Override
//    public String getPassword() {
//        return userEntity.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return userEntity.getEmail();
//    }
//
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
