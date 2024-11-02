package com.dart.product.repository;


import com.dart.product.dto_model.wishlists_model.WishlistDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistsRepo extends JpaRepository<WishlistDbModel,Long> {

}
