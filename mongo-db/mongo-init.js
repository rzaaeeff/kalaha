db.createUser(
    {
        user  : "kalaha_db_user",
        pwd   : "kalaha_db_pass",
        roles : [
            {
                role : "readWrite",
                db   : "kalaha_game_db"
            }
        ]
    }
)