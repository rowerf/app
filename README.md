<h1>Project proposal</h1>
Summary: an application that helps finding the best venue suggestions based on the user's foodpreferences. Made by: Rosan van der Werf 11030917

<h3>Problem statement</h3>
target audience: people that want to eat out but (problem:) don't really know what and where to eat at a place nearby that also takes into account foodpreferences like veganism, vegetarism or allergies/introlerances. This application also helps to more easily propose restaurants when wanting to eat with friends for example.

<h3>Solution</h3>
This app helps finding venue suggestions based on the user's location, in which it also ensures it meets the preferences, allergies and intolerances. 

![visual sketch](doc/sketch.PNG)

Main features:
- Register as new user and indicate food preferences. Or (auto)login as existing user
- Suggest a search and request that takes into account location and preferences
- See on a map with markers the possibilites where to eat, and also the user's own location (for approximity)
- - As visualisation (shown as a list) there is also a list with more details about the venues (like: name, what kind of food is served (kitchen), address, telephonenumber, and distance)
- When a marker is clicked, the name of the venue is shown and there is a possibility to see the travel directions
- When a venue in the list is clicked, the dialer will open with the phone number for easy reservationmaking or questions
- The user can change the preferences and username and has acces to instructions
- Autologin when the user is logged in and hasn't logged out
- (*) Indicate kitchen preferences. For example, when the user wants to eat French, can include this in the searchrequest
- (*) The possibility to search for other users and save them in a personalized friendslist. This to make it easier to combine preferences and base a searchrequest on the combined preferences.
- (*) Search for venues using an alternative location (for when the user is not yet at the place s/he wants to eat)
- (*) An aption to share a specific venue (maybe like a simple 'copy' of venuename and address)
- (*) Have more filter options when a venuerequest is made. Like, kitchenpreferences, but also if there's parking nearby, if dogs are allowed and the accesibility for people in wheelchairs
- (*) Acces to more detailed information like: price indications, reviews and ambiences, image, menus or a link to the venue's website. Or a possibility to make a reservation.



<h2>Prerequisites</h2>

Data sources:
- [Import Maps SDK](https://developers.google.com/maps/documentation/android-sdk/utility/) is used to import a map, an important part of this application's visualisation
- [An API to request restaurants that meet the search filter](https://docs.eet.nu/) an API that contains data about nearly all venues in The Netherlands. This will be used to fill the visualisation with usefull info for the user.

External components:
- [SQLite](https://sqlite.org/index.html) to store the user's logindata and preferences

Similar mobile apps:
- Google Maps
- Food delivery app

Hardest parts:
- Implementing a map into the app, mostly because I have never done something like that before
- Work with an API with a rather unclear documentation, which makes it hard to request and receive the data needed
