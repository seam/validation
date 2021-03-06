/**
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.validation.test.common.method.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * An exemplary model class representing a movie.
 * 
 * @author Gunnar Morling
 * 
 */
public class Movie {

    private final long id;

    private final String title;

    private final int runTime;

    private final String director;

    @NotNull
    private final Date releaseDate;

    public Movie(long id, String title, int runTime, String director, Date releaseDate) {

        this.id = id;
        this.title = title;
        this.runTime = runTime;
        this.director = director;
        this.releaseDate = releaseDate;
    }

    public Movie(long id, String title, int runTime) {

        this.id = id;
        this.title = title;
        this.runTime = runTime;
        this.director = null;
        this.releaseDate = null;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getRunTime() {
        return runTime;
    }

    public String getDirector() {
        return director;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", title=" + title + ", runTime=" + runTime + ", director=" + director + ", releaseDate="
                + releaseDate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((director == null) ? 0 : director.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((releaseDate == null) ? 0 : releaseDate.hashCode());
        result = prime * result + runTime;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movie other = (Movie) obj;
        if (director == null) {
            if (other.director != null)
                return false;
        } else if (!director.equals(other.director))
            return false;
        if (id != other.id)
            return false;
        if (releaseDate == null) {
            if (other.releaseDate != null)
                return false;
        } else if (!releaseDate.equals(other.releaseDate))
            return false;
        if (runTime != other.runTime)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

}
