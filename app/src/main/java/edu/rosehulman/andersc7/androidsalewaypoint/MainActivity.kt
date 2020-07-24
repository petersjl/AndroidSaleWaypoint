package edu.rosehulman.andersc7.androidsalewaypoint

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.OrientationEventListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.andersc7.androidsalewaypoint.ui.wishlist.WishlistFragment

class MainActivity : AppCompatActivity() {

	private lateinit var appBarConfiguration: AppBarConfiguration

	private var auth = FirebaseAuth.getInstance()
	private lateinit var authStateListener: FirebaseAuth.AuthStateListener

	// Request code for launching the sign in Intent.
	private val RC_SIGN_IN = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val toolbar: Toolbar = findViewById(R.id.toolbar)
		setSupportActionBar(toolbar)

		val fab: FloatingActionButton = findViewById(R.id.fab)
		fab.setOnClickListener { view ->
			Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show()
		}
		val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
		val navView: NavigationView = findViewById(R.id.nav_view)
		val navController = findNavController(R.id.nav_host_fragment)
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		appBarConfiguration = AppBarConfiguration(setOf(
				R.id.nav_wishlist, R.id.nav_sales, R.id.nav_stores), drawerLayout)
		setupActionBarWithNavController(navController, appBarConfiguration)
		navView.setupWithNavController(navController)

		initializeListeners()
	}

	override fun onStart() {
		super.onStart()
		auth.addAuthStateListener(authStateListener)
	}

	override fun onStop() {
		super.onStop()
		auth.removeAuthStateListener(authStateListener)
	}

	fun initializeListeners(){
		Log.d("tag", "Initializing listeners")
		authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
			val user = auth.currentUser
			Log.d("tag", "User is: $user")
			if (user == null){
				// Choose authentication providers
				val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

				// Create and launch sign-in intent
				startActivityForResult(
					AuthUI.getInstance()
						.createSignInIntentBuilder()
						.setAvailableProviders(providers)
						.setLogo(R.drawable.ic_launcher_foreground)
						.build(),
					RC_SIGN_IN)
			}
			else {
				val ft = supportFragmentManager.beginTransaction()
				ft.replace(R.id.nav_host_fragment, WishlistFragment())
				ft.commit()
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.main, menu)
		return true
	}

	override fun onSupportNavigateUp(): Boolean {
		val navController = findNavController(R.id.nav_host_fragment)
		return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
	}
}