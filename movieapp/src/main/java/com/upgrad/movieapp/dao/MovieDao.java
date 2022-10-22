package com.upgrad.movieapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upgrad.movieapp.entity.Movie;

public interface MovieDao extends JpaRepository<Movie, Integer> {

}
