package com.koshsu.githubsearch.data.database.dao

import androidx.room.*
import com.koshsu.githubsearch.data.database.model.Owner
import com.koshsu.githubsearch.data.database.model.Repo
import com.koshsu.githubsearch.data.database.model.RepoWithOwner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * This class provides general DAO for both entities [Repo], [Owner]
 * "Abstract class" is used instead of "interface" to provide ability
 * to use my own functions for insertion and reading data
 * */
@Dao
abstract class RepoAndOwnerDao {

    /** Public functions for database manipulations */

    fun insertReposWithOwner(repos: List<Repo>) {
        // delete previous data
        deleteAllRepos()
        deleteAllOwners()

        // save new data
        for (r in repos) {
            r.owner.repoId = r.id
            upsertOwner(r.owner)
        }
        insertRepos(repos)
    }

    fun getReposWithOwner() : Flow<List<Repo>> {
        val reposWithOwner = _getAllWithOwnerSync()
        val repos: MutableList<Repo> = mutableListOf()
        for (i in reposWithOwner) {
            i.repo.owner = i.owner
            repos.add(i.repo)
        }
        return flow { emit(repos) }
    }

    fun getRepoWithOwnerById(repoId: Long) : Flow<Repo> {
        val repoWithOwner = _getRepoWithOwnerById(repoId)
        val repo = repoWithOwner.repo
        repo.owner = repoWithOwner.owner
        return flow { emit(repo) }
    }


    /**
     *  Generated functions by Room
     *  "_"prefix means that these functions are private for this class
     *  (Room doesn't allow private functions)
     */

    // insert or update if exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertRepos(repos: List<Repo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertRepos(repos: List<Repo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertRepo(vararg repo: Repo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertOwner(owner: Owner)

    @Transaction
    @Query("SELECT * FROM Repo, Owner WHERE Repo.id = Owner.repoId ORDER BY Repo.stars DESC")
    abstract fun _getAllWithOwnerSync() : List<RepoWithOwner>

    @Transaction
    @Query("SELECT * FROM Repo INNER JOIN Owner ON Owner.repoId = Repo.id WHERE Repo.id = :repoId")
    abstract fun _getRepoWithOwnerById(repoId: Long) : RepoWithOwner

    @Query("DELETE FROM Repo")
    abstract fun deleteAllRepos()

    @Query("DELETE FROM Owner")
    abstract fun deleteAllOwners()

}