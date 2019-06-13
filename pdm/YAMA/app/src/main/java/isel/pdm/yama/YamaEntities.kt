package isel.pdm.yama

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity

@Entity(tableName = "profile", primaryKeys = ["login"])
data class Profile(
        val login : String,
        val name : String?,
        val email : String?,
        val followers : Int,
        val avatar_url : String
): Parcelable {

        companion object CREATOR : Parcelable.Creator<Profile> {
                override fun createFromParcel(source: Parcel): Profile {
                        return Profile(
                                source.readString()!!,
                                source.readString(),
                                source.readString(),
                                source.readInt(),
                                source.readString()!!)
                }

                override fun newArray(size: Int): Array<Profile?>  = arrayOfNulls(size)


        }
        override fun writeToParcel(dest: Parcel, flags: Int) {
                dest.writeString(login)
                dest.writeString(name)
                dest.writeString(email)
                dest.writeInt(followers)
                dest.writeString(avatar_url)
        }

        override fun describeContents(): Int = 0


}

@Entity(tableName = "team", primaryKeys = ["id"])
data class Team(
        val name : String?,
        val id : Int,
        val idOrg: String
)

@Entity(tableName = "membersOfTeam", primaryKeys = ["loginProfile", "idTeam"])
data class MembersOfTeam(
        val loginProfile: String,
        val idTeam : Int
)

