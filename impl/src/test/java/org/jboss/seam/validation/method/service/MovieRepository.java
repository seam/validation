/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.validation.method.service;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.seam.validation.AutoValidating;
import org.jboss.seam.validation.method.domain.Movie;

/**
 * An exemplary business service for which automatic method-level validation is
 * enabled.
 * 
 * @author Gunnar Morling
 * 
 */
@AutoValidating
public class MovieRepository
{

   private final Map<Long, Movie> sampleMovies = new TreeMap<Long, Movie>();

   public MovieRepository()
   {

      Movie movie = new Movie(1, "The Usual Suspects", 106, "Bryan Singer", new GregorianCalendar(1995, 7, 16).getTime());

      sampleMovies.put(movie.getId(), movie);

      movie = new Movie(2, "The Road", 160, "John Hillcoat", null);

      sampleMovies.put(movie.getId(), movie);
   }

   public @NotNull
   @Valid
   Set<Movie> findMoviesByDirector(@NotNull @Size(min = 3) String director)
   {

      Set<Movie> theValue = new HashSet<Movie>();

      for (Movie oneMovie : sampleMovies.values())
      {
         if (oneMovie.getDirector().equals(director))
         {
            theValue.add(oneMovie);
         }

      }

      return theValue;
   }

}