package blockbreaker
fun getResource(filename: String): String {
    return Game::class.java.getResource(filename).toString()
}
