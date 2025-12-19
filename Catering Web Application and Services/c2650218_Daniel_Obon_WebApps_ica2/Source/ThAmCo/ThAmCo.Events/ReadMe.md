Project Name: ThAmCo.Application
Description: ThreeAmigos Corp is an event management company that arranges and oversees a range of functions, including conferences, 
			parties and weddings for its clients. As well as logistics, ThreeAmigos Corp organises the catering and staffing for events. 
			You have been given the responsibility of constructing a new intranet-based prototype system for the events management team.  

=== ThAmCo.Catering Documentation ===
1. Unable to scaffold code properly.
Solution: Asked help from tutor about the problem, and he suggested in downgrading the version of Microsoft.EntityFrameworkCore 
		and its other associated packages (sqlite, sqlserver and tools) from 6.0.24 to 6.0.23.
		To check this, just click on the project (eg. ThAmCo.Catering or ThAmCo.Events) and a window with code will appear with the current verisons used.

2. Faced a problem where MenuId and FoodItemId were not set as foreign keys in MenuFoodItem, thus having a wrong relationship compared to the ERD provided.
Solution: Used DB Browser to check its relationship and referenced the lab tutorial to implement a composite key.

3. Problem where Key for a domain is not set, even though "Id" was included into the key names such as MenuId, FoodItemId and FoodBookingId.
Solution: Checked on Microsoft Learn (Title: "Keys - EF Core") on how to resolve issue. 
		The term [Key] should have been set on the particular Ids.

4. Unable to implement List of FoodItems properly.
Solution: Referenced an example from Vista tutorial which is similar on now to implement a list of an entity.


=== ThAmCo.Events Documentation ===
1. Unable to connect ThAmCo.Venus EventType entity to ThAmCo.Events for Events.
Solution: By referencing Vista tutorial, a wrong localhost address was used (used Th.Am.Co.Events address previously before fix). 
		Moreover, did not run ThAmCo.Venues with ThAmCo.Events at the same time during runtime by using "Configure Startup Project".

2. Unable to delete an Event with its associated Guest.
Solution: Referenced Microsft Learn (Title: "Cascade Delete - EF Core") to resolve this issue.
		To enable the delete, DeleteBehavior.Restrict has to be changed into DeleteBehavior.Cascade in EventsDbContext.cs.

3. Trouble in properly implementing the html and css designs I desire.
Solution: Followed and referenced the HTML and CSS guides on W3Schools, 
		such as how to apply a design to multiple instances of an object (eg. header and body) and designing a webpage individually.

4. Trouble in implementing a dropdown for EventTypes and Guest
Solution: Reference both Dot Net Tutorials (Title: "ViewBag in ASP.NET Core MVC with examples") and Vista tutotial on its implementation.

5. Trouble implementing Soft Delete function for Events eventhough following the lecture example.
Solution: Rewatched the lecture on soft delete and noticed that a migration must be done after the boolean "IsDeleted" added into Event.cs.


=== Report Documentation ===
1. Trouble in identifying the security features that are to be planned in report.
Solution: Research 0Auth 2.0 on its application and its principles and how it works and implementd, which will be mentioned in report.