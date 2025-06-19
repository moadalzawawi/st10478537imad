package com.example.madpracticumexamst10478537

// Student Number: ST10478537
// Full Name:Moad Elzawawi

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Declare and initialize parallel arrays for song data
    private var songTitles = mutableListOf<String>()
    private var artistNames = mutableListOf<String>()
    private var ratings = mutableListOf<Int>()
    private var comments = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize with sample data
        initializeSampleData()

        // Set up button listeners
        setupButtonListeners()
    }

    // Method to initialize sample data
    private fun initializeSampleData() {
        songTitles.addAll(listOf("Song A", "Song B", "Song C", "Song D"))
        artistNames.addAll(listOf("Artist A", "Artist B", "Artist C", "Artist D"))
        ratings.addAll(listOf(4, 1, 2, 3))
        comments.addAll(listOf("Rap", "Dance song", "Best Love song", "Memories"))
    }

    // Method to set up button listeners
    private fun setupButtonListeners() {
        findViewById<Button>(R.id.btnAddToPlaylist).setOnClickListener {
            showAddSongDialog()
        }

        findViewById<Button>(R.id.btnViewDetails).setOnClickListener {
            navigateToDetailedView()
        }

        findViewById<Button>(R.id.btnExitApp).setOnClickListener {
            exitApp()
        }
    }

    // Method to show dialog for adding new song
    private fun showAddSongDialog() {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
        }

        val titleInput = EditText(this).apply { hint = "Song Title" }
        val artistInput = EditText(this).apply { hint = "Artist Name" }
        val ratingInput = EditText(this).apply {
            hint = "Rating (1-5)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        val commentInput = EditText(this).apply { hint = "Comments" }

        dialogLayout.addView(titleInput)
        dialogLayout.addView(artistInput)
        dialogLayout.addView(ratingInput)
        dialogLayout.addView(commentInput)

        AlertDialog.Builder(this)
            .setTitle("Add New Song")
            .setView(dialogLayout)
            .setPositiveButton("Add") { _, _ ->
                addSongToPlaylist(
                    titleInput.text.toString(),
                    artistInput.text.toString(),
                    ratingInput.text.toString(),
                    commentInput.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Method to add song to playlist with error handling
    private fun addSongToPlaylist(title: String, artist: String, ratingStr: String, comment: String) {
        try {
            // Error handling for empty fields
            if (title.isEmpty() || artist.isEmpty() || ratingStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return
            }

            val rating = ratingStr.toInt()

            // Error handling for rating range
            if (rating < 1 || rating > 5) {
                Toast.makeText(this, "Rating must be between 1 and 5", Toast.LENGTH_SHORT).show()
                return
            }

            // Add to arrays
            songTitles.add(title)
            artistNames.add(artist)
            ratings.add(rating)
            comments.add(comment)

            Toast.makeText(this, "Song added successfully!", Toast.LENGTH_SHORT).show()

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Please enter a valid number for rating", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error adding song: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to navigate to detailed view screen
    private fun navigateToDetailedView() {
        val intent = Intent(this, DetailedViewActivity::class.java).apply {
            putStringArrayListExtra("songTitles", ArrayList(songTitles))
            putStringArrayListExtra("artistNames", ArrayList(artistNames))
            putIntegerArrayListExtra("ratings", ArrayList(ratings))
            putStringArrayListExtra("comments", ArrayList(comments))
        }
        startActivity(intent)
    }

    // Method to exit the app
    private fun exitApp() {
        finish()
    }
}

// Detailed View Activity
class DetailedViewActivity : AppCompatActivity() {

    private lateinit var songTitles: ArrayList<String>
    private lateinit var artistNames: ArrayList<String>
    private lateinit var ratings: ArrayList<Int>
    private lateinit var comments: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_view)

        // Get data from intent
        songTitles = intent.getStringArrayListExtra("songTitles") ?: arrayListOf()
        artistNames = intent.getStringArrayListExtra("artistNames") ?: arrayListOf()
        ratings = intent.getIntegerArrayListExtra("ratings") ?: arrayListOf()
        comments = intent.getStringArrayListExtra("comments") ?: arrayListOf()

        setupDetailedViewButtons()
    }

    // Method to set up detailed view buttons
    private fun setupDetailedViewButtons() {
        findViewById<Button>(R.id.btnDisplaySongs).setOnClickListener {
            displayAllSongs()
        }

        findViewById<Button>(R.id.btnCalculateAverage).setOnClickListener {
            calculateAndDisplayAverage()
        }

        findViewById<Button>(R.id.btnReturnMain).setOnClickListener {
            finish()
        }
    }

    // Method to display all songs using loop
    private fun displayAllSongs() {
        if (songTitles.isEmpty()) {
            Toast.makeText(this, "No songs in playlist", Toast.LENGTH_SHORT).show()
            return
        }

        val songList = StringBuilder("Playlist Songs:\n\n")

        // Use loop to iterate through all songs
        for (i in songTitles.indices) {
            songList.append("${i + 1}. ${songTitles[i]}\n")
            songList.append("   Artist: ${artistNames[i]}\n")
            songList.append("   Rating: ${ratings[i]}/5\n")
            songList.append("   Comments: ${comments[i]}\n\n")
        }

        AlertDialog.Builder(this)
            .setTitle("Song Details")
            .setMessage(songList.toString())
            .setPositiveButton("OK", null)
            .show()
    }

    // Method to calculate and display average rating using loop
    private fun calculateAndDisplayAverage() {
        if (ratings.isEmpty()) {
            Toast.makeText(this, "No songs to calculate average", Toast.LENGTH_SHORT).show()
            return
        }

        var total = 0

        // Use loop to calculate sum of all ratings
        for (rating in ratings) {
            total += rating
        }

        val average = total.toDouble() / ratings.size

        AlertDialog.Builder(this)
            .setTitle("Average Rating")
            .setMessage(String.format("Average rating: %.2f/5\nTotal songs: %d", average, ratings.size))
            .setPositiveButton("OK", null)
            .show()
    }
}