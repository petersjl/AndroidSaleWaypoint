package edu.rosehulman.andersc7.androidsalewaypoint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import edu.rosehulman.andersc7.androidsalewaypoint.ui.SignInFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.browse.*
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.Game
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.GameAdapter
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.GameFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.StoreType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_add.view.*
import kotlinx.android.synthetic.main.dialog_edit.view.*
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), GameAdapter.OnGameSelectedListener, NavigationView.OnNavigationItemSelectedListener {

	private lateinit var appBarConfiguration: AppBarConfiguration

	private var auth = FirebaseAuth.getInstance()
	private var gamesRef = FirebaseFirestore.getInstance().collection("Games")
	private lateinit var authStateListener: FirebaseAuth.AuthStateListener
	private lateinit var drawerLayout: DrawerLayout
	private lateinit var drawerToggle: ActionBarDrawerToggle

	private var currentGame: Game? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		fab.setOnClickListener { view ->
			showAddDialog()
		}
		setUpToolbar()
		initializeListeners()
	}

	//Show the dialog to add a game
	private fun showAddDialog(){
		val builder = AlertDialog.Builder(this, R.style.AlertDialog)

		//Set options
		builder.setTitle("Add a game")
		val view = LayoutInflater.from(this).inflate(R.layout.dialog_add, null, false)
		val dialog: AlertDialog = builder.setView(view)
			.setPositiveButton("Add", null)
			.setNegativeButton(android.R.string.cancel, null)
			.create()


		dialog.show()
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

			//Get text inputs
			val title = view.add_title_text.text.toString()
			val dev = view.add_developer_text.text.toString()
			var desc = view.add_description_text.text.toString()

			//Check text inputs
			if (title == "") {message("Title cannot be empty"); return@setOnClickListener}
			if (dev == "") {message("Developer cannot be empty"); return@setOnClickListener}
			if (desc == "") desc = "Add a description for this game."

			//Get enables
			val onSteam = view.add_enabled_steam.isChecked
			val onPlay = view.add_enabled_playstation.isChecked
			val onXbox = view.add_enabled_xbox.isChecked
			val onNin = view.add_enabled_nintendo.isChecked
			val onItch = view.add_enabled_itch.isChecked

			//Get number inputs
			val textPriceSteam = view.add_price_steam.text.toString()
			val textPricePlay = view.add_price_playstation.text.toString()
			val textPriceXbox = view.add_price_xbox.text.toString()
			val textPriceNin = view.add_price_nintendo.text.toString()
			val textPriceItch = view.add_price_itch.text.toString()

			val textSaleSteam = view.add_sale_steam.text.toString()
			val textSalePlay = view.add_sale_playstation.text.toString()
			val textSaleXbox = view.add_sale_xbox.text.toString()
			val textSaleNin = view.add_sale_nintendo.text.toString()
			val textSaleItch = view.add_sale_itch.text.toString()

			//Check nulls and convert if enabled
			val priceSteam : Double = if (onSteam) if (textPriceSteam != "") textPriceSteam.toDouble() else {message("Steam price cannot be empty"); return@setOnClickListener} else 0.0
			val pricePlay : Double	= if (onPlay) if (textPricePlay != "") textPricePlay.toDouble() else {message("PlayStation price cannot be empty"); return@setOnClickListener} else 0.0
			val priceXbox : Double 	= if (onXbox) if (textPriceXbox != "") textPriceXbox.toDouble() else {message("Xbox price cannot be empty"); return@setOnClickListener} else 0.0
			val priceNin : Double 	= if (onNin) if (textPriceNin != "") textPriceNin.toDouble() else {message("Nintendo price cannot be empty"); return@setOnClickListener} else 0.0
			val priceItch : Double 	= if (onItch) if (textPriceItch != "") textPriceItch.toDouble() else {message("Itch price cannot be empty"); return@setOnClickListener} else 0.0

			val saleSteam : Double 	= if (onSteam) if (textSaleSteam != "") (textSaleSteam.toDouble() / 100.0) else {message("Steam sale cannot be empty"); return@setOnClickListener} else 0.0
			val salePlay : Double 	= if (onPlay) if (textSalePlay != "") (textSalePlay.toDouble() / 100.0) else {message("PlayStation sale cannot be empty"); return@setOnClickListener} else 0.0
			val saleXbox : Double 	= if (onXbox) if (textSaleXbox != "") (textSaleXbox.toDouble() / 100.0) else {message("Xbox sale cannot be empty"); return@setOnClickListener} else 0.0
			val saleNin : Double 	= if (onNin) if (textSaleNin != "") (textSaleNin.toDouble() / 100.0) else {message("Nintendo sale cannot be empty"); return@setOnClickListener} else 0.0
			val saleItch : Double 	= if (onItch) if (textSaleItch != "") (textSaleItch.toDouble() / 100.0) else {message("Itch sale cannot be empty"); return@setOnClickListener} else 0.0

			//----Turns out these text boxes don't accept negatives
			//Check price values
//			if (priceSteam < 0) {message("Steam price must be positive"); return@setOnClickListener}
//			if (pricePlay < 0) {message("PlayStation price must be positive"); return@setOnClickListener}
//			if (priceXbox < 0) {message("Xbox price must be positive"); return@setOnClickListener}
//			if (priceNin < 0) {message("Nintendo price must be positive"); return@setOnClickListener}
//			if (priceItch < 0) {message("Itch price must be positive"); return@setOnClickListener}
			
			//Check sale upperbound
			if (saleSteam > 1) {message("Steam sale must be less than 101"); return@setOnClickListener}
			if (salePlay > 1) {message("PlayStation sale must be less than 101"); return@setOnClickListener}
			if (saleXbox > 1) {message("Xbox sale must be less than 101"); return@setOnClickListener}
			if (saleNin > 1) {message("Steam sale must be less than 101"); return@setOnClickListener}
			if (saleItch > 1) {message("Itch sale must be less than 101"); return@setOnClickListener}

			//----Turns out these text boxes don't accept negatives
			//Check sale lowerbound
//			if (saleSteam < 0) {message("Steam sale must be positive"); return@setOnClickListener}
//			if (salePlay < 0) {message("PlayStation sale must be positive"); return@setOnClickListener}
//			if (saleXbox < 0) {message("Xbox sale must be positive"); return@setOnClickListener}
//			if (saleNin < 0) {message("Nintendo sale must be positive"); return@setOnClickListener}
//			if (saleItch < 0) {message("Itch sale must be positive"); return@setOnClickListener}

			val stores: ArrayList<StoreType> = ArrayList()
			if (onSteam) stores.add(StoreType.STEAM)
			if (onPlay) stores.add(StoreType.PLAYSTATION)
			if (onXbox) stores.add(StoreType.XBOX)
			if (onNin) stores.add(StoreType.NINTENDO)
			if (onItch) stores.add(StoreType.ITCH)
			val sale = (saleSteam > 0 || salePlay > 0 || saleXbox > 0 || saleNin > 0 || saleItch > 0)

			val data = hashMapOf(
				Constants.FIELD_TITLE to title,
				Constants.FIELD_DEVELOPER to dev,
				Constants.FIELD_DESCRIPTION to desc,
				Constants.FIELD_STORES to stores,
				Constants.FIELD_SALE to sale,
				Constants.FIELD_WISHLISTERS to ArrayList<String>()
			)
			
			gamesRef.add(data).addOnSuccessListener {doc: DocumentReference? ->
				val listings = doc?.collection("Listings")

				//Create whatever store listings are required
				if(onSteam) {
					listings?.document("steam")?.set(
						hashMapOf(
							"store" to "STEAM",
							"price" to priceSteam,
							"sale" to saleSteam
						)
					)
				}
				if(onPlay) {
					listings?.document("playstation")?.set(
						hashMapOf(
							"store" to "PLAYSTATION",
							"price" to pricePlay,
							"sale" to salePlay
						)
					)
				}
				if(onXbox) {
					listings?.document("xbox")?.set(
						hashMapOf(
							"store" to "XBOX",
							"price" to priceXbox,
							"sale" to saleXbox
						)
					)
				}
				if(onNin) {
					listings?.document("nintendo")?.set(
						hashMapOf(
							"store" to "NINTENDO",
							"price" to priceNin,
							"sale" to saleNin
						)
					)
				}
				if(onItch) {
					listings?.document("itch")?.set(
						hashMapOf(
							"store" to "ITCH",
							"price" to priceItch,
							"sale" to saleItch
						)
					)
				}

			}

			dialog.dismiss()
		}
	}

	//Show the dialog to edit a game
	private fun showEditDialog(){
		//Basic error check if the fab gets set incorrectly
		if (currentGame == null){
			message("The edit dialog tried to show without a game")
			return
		}else {
			val game = currentGame!!

			//Set up view
			val view = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null, false)
			view.edit_title.text = game.title
			view.edit_developer.text = game.developer
			view.edit_description_text.setText(game.description)
			for (listing in game.listings){
				when (listing.store){
					StoreType.STEAM -> {
						view.edit_enabled_steam.isChecked = true
						view.edit_price_steam.setText(String.format("%.2f", listing.price))
						view.edit_sale_steam.setText((listing.sale * 100).roundToInt().toString())
					}
					StoreType.PLAYSTATION -> {
						view.edit_enabled_playstation.isChecked = true
						view.edit_price_playstation.setText(String.format("%.2f", listing.price))
						view.edit_sale_playstation.setText((listing.sale * 100).roundToInt().toString())
					}
					StoreType.XBOX -> {
						view.edit_enabled_xbox.isChecked = true
						view.edit_price_xbox.setText(String.format("%.2f", listing.price))
						view.edit_sale_xbox.text
						view.edit_sale_xbox.setText((listing.sale * 100).roundToInt().toString())
					}
					StoreType.NINTENDO -> {
						view.edit_enabled_nintendo.isChecked = true
						view.edit_price_nintendo.setText(String.format("%.2f", listing.price))
						view.edit_sale_nintendo.setText((listing.sale * 100).roundToInt().toString())
					}
					StoreType.ITCH -> {
						view.edit_enabled_itch.isChecked = true
						view.edit_price_itch.setText(String.format("%.2f", listing.price))
						view.edit_sale_itch.setText((listing.sale * 100).roundToInt().toString())
					}
				}
			}


			//Set up dialog
			val builder = AlertDialog.Builder(this, R.style.AlertDialog)
			builder.setTitle("Edit this game")
			val dialog: AlertDialog = builder.setView(view)
				.setPositiveButton("Commit", null)
				.setNegativeButton(android.R.string.cancel, null)
				.create()


			dialog.show()
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

				//Get text inputs
				var desc = view.edit_description_text.text.toString()

				//Check text inputs
				if (desc == "") desc = "Add a description for this game."

				//Get enables
				val onSteam = view.edit_enabled_steam.isChecked
				val onPlay = view.edit_enabled_playstation.isChecked
				val onXbox = view.edit_enabled_xbox.isChecked
				val onNin = view.edit_enabled_nintendo.isChecked
				val onItch = view.edit_enabled_itch.isChecked

				//Get number inputs
				val textPriceSteam = view.edit_price_steam.text.toString()
				val textPricePlay = view.edit_price_playstation.text.toString()
				val textPriceXbox = view.edit_price_xbox.text.toString()
				val textPriceNin = view.edit_price_nintendo.text.toString()
				val textPriceItch = view.edit_price_itch.text.toString()

				val textSaleSteam = view.edit_sale_steam.text.toString()
				val textSalePlay = view.edit_sale_playstation.text.toString()
				val textSaleXbox = view.edit_sale_xbox.text.toString()
				val textSaleNin = view.edit_sale_nintendo.text.toString()
				val textSaleItch = view.edit_sale_itch.text.toString()

				//Check nulls and convert if enabled
				val priceSteam : Double = if (onSteam) if (textPriceSteam != "") textPriceSteam.toDouble() else {message("Steam price cannot be empty"); return@setOnClickListener} else 0.0
				val pricePlay : Double	= if (onPlay) if (textPricePlay != "") textPricePlay.toDouble() else {message("PlayStation price cannot be empty"); return@setOnClickListener} else 0.0
				val priceXbox : Double 	= if (onXbox) if (textPriceXbox != "") textPriceXbox.toDouble() else {message("Xbox price cannot be empty"); return@setOnClickListener} else 0.0
				val priceNin : Double 	= if (onNin) if (textPriceNin != "") textPriceNin.toDouble() else {message("Nintendo price cannot be empty"); return@setOnClickListener} else 0.0
				val priceItch : Double 	= if (onItch) if (textPriceItch != "") textPriceItch.toDouble() else {message("Itch price cannot be empty"); return@setOnClickListener} else 0.0

				val saleSteam : Double 	= if (onSteam) if (textSaleSteam != "") (textSaleSteam.toDouble() / 100.0) else {message("Steam sale cannot be empty"); return@setOnClickListener} else 0.0
				val salePlay : Double 	= if (onPlay) if (textSalePlay != "") (textSalePlay.toDouble() / 100.0) else {message("PlayStation sale cannot be empty"); return@setOnClickListener} else 0.0
				val saleXbox : Double 	= if (onXbox) if (textSaleXbox != "") (textSaleXbox.toDouble() / 100.0) else {message("Xbox sale cannot be empty"); return@setOnClickListener} else 0.0
				val saleNin : Double 	= if (onNin) if (textSaleNin != "") (textSaleNin.toDouble() / 100.0) else {message("Nintendo sale cannot be empty"); return@setOnClickListener} else 0.0
				val saleItch : Double 	= if (onItch) if (textSaleItch != "") (textSaleItch.toDouble() / 100.0) else {message("Itch sale cannot be empty"); return@setOnClickListener} else 0.0

				//Check sale upperbound
				if (saleSteam > 1) {message("Steam sale must be less than 101"); return@setOnClickListener}
				if (salePlay > 1) {message("PlayStation sale must be less than 101"); return@setOnClickListener}
				if (saleXbox > 1) {message("Xbox sale must be less than 101"); return@setOnClickListener}
				if (saleNin > 1) {message("Steam sale must be less than 101"); return@setOnClickListener}
				if (saleItch > 1) {message("Itch sale must be less than 101"); return@setOnClickListener}

				val stores: ArrayList<StoreType> = ArrayList()
				if (onSteam) stores.add(StoreType.STEAM)
				if (onPlay) stores.add(StoreType.PLAYSTATION)
				if (onXbox) stores.add(StoreType.XBOX)
				if (onNin) stores.add(StoreType.NINTENDO)
				if (onItch) stores.add(StoreType.ITCH)

				val sale = (saleSteam > 0 || salePlay > 0 || saleXbox > 0 || saleNin > 0 || saleItch > 0)
				gamesRef.document(game.id).set(hashMapOf(
					Constants.FIELD_DESCRIPTION to desc,
					Constants.FIELD_STORES to stores,
					Constants.FIELD_SALE to sale
				), SetOptions.merge())

				//Set store listings
				val listings = gamesRef.document(game.id).collection("Listings")
				if(onSteam) {
					listings.document("steam").set(
						hashMapOf(
							"store" to "STEAM",
							"price" to priceSteam,
							"sale" to saleSteam
						)
					)
				}else{
					listings.document("steam").delete()
				}
				if(onPlay) {
					listings.document("playstation").set(
						hashMapOf(
							"store" to "PLAYSTATION",
							"price" to pricePlay,
							"sale" to salePlay
						)
					)
				}else{
					listings.document("playstation").delete()
				}
				if(onXbox) {
					listings.document("xbox").set(
						hashMapOf(
							"store" to "XBOX",
							"price" to priceXbox,
							"sale" to saleXbox
						)
					)
				}else{
					listings.document("xbox").delete()
				}
				if(onNin) {
					listings.document("nintendo").set(
						hashMapOf(
							"store" to "NINTENDO",
							"price" to priceNin,
							"sale" to saleNin
						)
					)
				}else{
					listings.document("nintendo").delete()
				}
				if(onItch) {
					listings.document("itch").set(
						hashMapOf(
							"store" to "ITCH",
							"price" to priceItch,
							"sale" to saleItch
						)
					)
				}else{
					listings.document("itch").delete()
				}

				dialog.dismiss()
			}
		}
	}

	//Shortcut to display short Toast message
	private fun message(m: String){
		Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
	}

	//Sets the properties of the toolbar
	private fun setUpToolbar(){
		val toolbar: Toolbar = findViewById(R.id.toolbar)
		setSupportActionBar(toolbar)

		drawerLayout = findViewById(R.id.drawer_layout)
		val navView: NavigationView = findViewById(R.id.nav_view)

		drawerToggle = ActionBarDrawerToggle(
			this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
		)
		drawerLayout.addDrawerListener(drawerToggle)
		drawerToggle.syncState()
		navView.setNavigationItemSelectedListener(this)
	}

	override fun onStart() {
		super.onStart()
		auth.addAuthStateListener(authStateListener)
	}

	override fun onStop() {
		super.onStop()
		auth.removeAuthStateListener(authStateListener)
	}

	//Set up the various listeners to be active in the activity
	fun initializeListeners(){
		Log.d("tag", "Initializing listeners")
		authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
			val user = auth.currentUser
			Log.d("tag", "User is: $user")
			if (user == null){
				supportActionBar?.hide()
				fab.hide()
				val ft = supportFragmentManager.beginTransaction()
				ft.replace(R.id.fragment_container, SignInFragment())
				ft.commit()
			}
			else {
				toolbar.title = "All Games"
				supportActionBar?.show()
				fab.show()
				val ft = supportFragmentManager.beginTransaction()
				ft.replace(R.id.fragment_container,
//					WishlistFragment()
					BrowseFragment(this.auth.currentUser!!.uid, FilterAll())
				)
				ft.commit()
			}
		}
		supportFragmentManager.addOnBackStackChangedListener {
			if (supportFragmentManager.backStackEntryCount == 0){
				fab.setImageResource(R.drawable.ic_add)
				fab.setOnClickListener { showAddDialog() }
				currentGame = null
			} else{
				fab.setImageResource(R.drawable.ic_edit)
				fab.setOnClickListener { showEditDialog() }
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.main, menu)
		return true
	}


	override fun onGameSelected(game: Game) {
		Log.d(Constants.TAG, "Game selected: ${game.title}")
		currentGame = game
		val gameFragment = GameFragment.newInstance(game, auth.currentUser!!.uid)
		val ft = this.supportFragmentManager.beginTransaction()
		ft.replace(R.id.fragment_container, gameFragment)
		ft.addToBackStack("game")
		ft.commit()
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		var filter: GameFilter? = null
		// Handle navigation view item clicks here.
		when (item.itemId) {
			R.id.nav_all -> {
				filter = FilterAll()
				toolbar.title = "All Games"
			}
			R.id.nav_wishlist -> {
				filter = FilterWishlist()
				toolbar.title = "Wishlist"
			}
			R.id.nav_sales -> {
				filter = FilterSale()
				toolbar.title = "What's on sale"
			}
			R.id.nav_log_out ->
				auth.signOut()
			R.id.nav_steam -> {
				filter = FilterStore(StoreType.STEAM)
				toolbar.title = "Steam"
			}
			R.id.nav_playstation -> {
				filter = FilterStore(StoreType.PLAYSTATION)
				toolbar.title = "PlayStation"
			}
			R.id.nav_xbox -> {
				filter = FilterStore(StoreType.XBOX)
				toolbar.title = "Xbox"
			}
			R.id.nav_nintendo -> {
				filter = FilterStore(StoreType.NINTENDO)
				toolbar.title = "Nintendo"
			}
			R.id.nav_itch -> {
				filter = FilterStore(StoreType.ITCH)
				toolbar.title = "Itch"
			}
		}
		if (filter != null) {
			val ft = supportFragmentManager.beginTransaction()
			ft.replace(R.id.fragment_container, BrowseFragment(this.auth.currentUser!!.uid, filter))
			while (supportFragmentManager.backStackEntryCount > 0){
				supportFragmentManager.popBackStackImmediate()
			}
			ft.commit()
		}
		drawer_layout.closeDrawer(GravityCompat.START)
		return true
	}
}