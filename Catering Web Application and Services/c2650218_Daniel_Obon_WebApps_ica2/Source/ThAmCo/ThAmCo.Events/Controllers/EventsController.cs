using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using ThAmCo.Events.Data;
using ThAmCo.Events.Domain;
using ThAmCo.Events.DTO;
using ThAmCo.Events.Models;

using System.Net.Http.Json;

namespace ThAmCo.Events.Controllers
{
    public class EventsController : Controller
    {
        private readonly EventsDbContext _context;

        public EventsController(EventsDbContext context)
        {
            _context = context;
        }

        // GET: Events
        /// <summary>
        /// Returns/gets all events
        /// </summary>
        /// <returns></returns>
        public async Task<IActionResult> Index()
        {
              List<EventViewModel> events = await _context.Events.Select(e => new EventViewModel
              {
                  EventId = e.EventId,
                  EventName = e.EventName,
                  EventTitle = e.EventTitle,
                  EventDate = e.EventDate,
                  EventTypeId = e.EventId,
              }).ToListAsync();

            return View(events);
        }

        // GET: Events/Details/5
        /// <summary>
        /// Returns/gets an event of a particular id in Details
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.Events == null)
            {
                return NotFound();
            }

            var @event = await _context.Events
                .FirstOrDefaultAsync(m => m.EventId == id);
            if (@event == null)
            {
                return NotFound();
            }

            return View(@event);
        }

        // GET: Events/Create
        /// <summary>
        /// Create event display
        /// </summary>
        /// <returns></returns>
        public async Task<IActionResult> Create()
        {
            List<EventTypeDTO> eventTypes = await GetEventTypes();
            // Drop down for Event Types
            ViewBag.EventTypes = new SelectList(eventTypes, "Id", "Title");

            // Retrieve a list of guests
            List<Guest> guests = await _context.Guests.ToListAsync();
            ViewBag.Guests = new SelectList(guests, "GuestId", "GuestName");

            return View();
        }

        // POST: Events/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to
        /// <summary>
        /// Creates an event and guest booking in Create (only able to add one guest at a time)
        /// </summary>
        /// <param name="event"></param>
        /// <returns></returns>
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("EventId,EventName,EventTitle,EventDate,FoodBookingId,EventTypeId,ReservationId,GuestBookings")] Event @event)
        {
            if (ModelState.IsValid)
            {
                // Add the event to the context
                _context.Add(@event);

                // Save changes to generate EventId
                await _context.SaveChangesAsync();

                // Check if there are guest bookings
                if (@event.GuestBookings != null && @event.GuestBookings.Any())
                {
                    try
                    {
                        // Save guest bookings, ensuring no duplicates
                        foreach (var guestBooking in @event.GuestBookings)
                        {
                            guestBooking.EventId = @event.EventId;

                            // Check if the combination of EventId and GuestId already exists
                            if (!_context.GuestBookings.Any(gb => gb.EventId == guestBooking.EventId && gb.GuestId == guestBooking.GuestId))
                            {
                                _context.GuestBookings.Add(guestBooking);
                            }
                        }

                        await _context.SaveChangesAsync();
                    }
                    catch (DbUpdateException ex)
                    {
                        // Log or handle the exception
                        ModelState.AddModelError(string.Empty, "Unable to save guest bookings. Duplicate entries detected.");
                        return View(@event);
                    }
                }

                return RedirectToAction(nameof(Index));
            }

            // Repopulate dropdown lists if validation fails
            List<EventTypeDTO> eventTypes = await GetEventTypes();
            ViewBag.EventTypes = new SelectList(eventTypes, "Id", "Title");

            List<Guest> guests = await _context.Guests.ToListAsync();
            ViewBag.Guests = new SelectList(guests, "GuestId", "GuestName");

            return View(@event);
        }




        // GET: Events/Edit/5
        /// <summary>
        /// Returns/gets an event of a particular id in Edit
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.Events == null)
            {
                return NotFound();
            }

            var @event = await _context.Events
                .Include(e => e.GuestBookings)
                .FirstOrDefaultAsync(m => m.EventId == id);

            if (@event == null)
            {
                return NotFound();
            }

            // Retrieve a list of event types
            List<EventTypeDTO> eventTypes = await GetEventTypes();
            ViewBag.EventTypes = new SelectList(eventTypes, "Id", "Title", @event.EventTypeId);

            // Retrieve a list of guests
            List<Guest> guests = await _context.Guests.ToListAsync();
            ViewBag.Guests = new SelectList(guests, "GuestId", "GuestName");

            return View(@event);
        }

        // POST: Events/Edit/5
        /// <summary>
        /// Creates a new event in Edit
        /// </summary>
        /// <param name="id"></param>
        /// <param name="event"></param>
        /// <returns></returns>
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("EventId,EventName,EventTitle,EventDate,FoodBookingId,EventTypeId,ReservationId, GuestBookings")] Event @event)
        {
            if (id != @event.EventId)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    // Remove existing guest bookings and add the selected ones
                    var existingGuestBookings = await _context.GuestBookings
                        .Where(gb => gb.EventId == @event.EventId)
                        .ToListAsync();

                    _context.GuestBookings.RemoveRange(existingGuestBookings);

                    foreach (var guestBooking in @event.GuestBookings)
                    {
                        guestBooking.EventId = @event.EventId;
                        _context.GuestBookings.Add(guestBooking);
                    }

                    _context.Update(@event);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!EventExists(@event.EventId))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }

            // Repopulate dropdown lists if validation fails
            List<EventTypeDTO> eventTypes = await GetEventTypes();
            ViewBag.EventTypes = new SelectList(eventTypes, "Id", "Title", @event.EventTypeId);

            List<Guest> guests = await _context.Guests.ToListAsync();
            ViewBag.Guests = new SelectList(guests, "GuestId", "GuestName");

            return View(@event);
        }



        // GET: Events/Delete/5
        /// <summary>
        /// Returns/gets an event following an id in Delete
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.Events == null)
            {
                return NotFound();
            }

            var @event = await _context.Events
                .FirstOrDefaultAsync(m => m.EventId == id);
            if (@event == null)
            {
                return NotFound();
            }

            return View(@event);
        }

        // POST: Events/Delete/5
        /// <summary>
        /// Deletes an event following an id in Delete (Including soft delete implementation for Events)
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.Events == null)
            {
                return Problem("Entity set 'EventsDbContext.Events'  is null.");
            }
            var @event = await _context.Events.FindAsync(id);
            if (@event != null)
            {
                @event.IsDeleted = true;
                await _context.SaveChangesAsync();
                // _context.Events.Remove(@event);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        /// <summary>
        /// Used to book a guest onto event following guestid and eventid respectively
        /// </summary>
        /// <param name="guestId"></param>
        /// <param name="eventId"></param>
        /// <returns></returns>
        public async Task<IActionResult> BookGuestToEvent(int guestId, int eventId)
        {
            // Retrieve guest and event from the database
            var guest = await _context.Guests.FindAsync(guestId);
            var @event = await _context.Events.FindAsync(eventId);

            if (guest != null && @event != null)
            {
                // Use navigation properties to associate the guest with the event
                @event.GuestBookings.Add(new GuestBooking { Guest = guest, Event = @event });

                // Save changes to the database
                await _context.SaveChangesAsync();

                // Redirect or return a response
                return RedirectToAction("Index", "Events");
            }

            // Handle errors or return a response
            return NotFound(); // or another appropriate response
        }


        /// <summary>
        /// Method to get event types from ThAmCo.Venues according its localhost (Must run during runtime ThAmCo.Venues to work)
        /// </summary>
        /// <returns></returns>
        /// <exception cref="ApplicationException"></exception>
        private async Task<List<EventTypeDTO>> GetEventTypes()
        {
            List<EventTypeDTO> eventTypes = new();

            HttpClient client = new HttpClient();
            client.BaseAddress = new System.Uri("https://localhost:7088/");
            client.DefaultRequestHeaders.Accept.ParseAdd("application/json");

            HttpResponseMessage response = await client.GetAsync("api/EventTypes");
            if(response.IsSuccessStatusCode)
            {
                eventTypes = await response.Content.ReadAsAsync<List<EventTypeDTO>>();
            }
            else
            {
                if(response.StatusCode == System.Net.HttpStatusCode.NotFound)
                {
                    return eventTypes;
                }
                else
                {
                    throw new ApplicationException("Unable to call API: " + response.ReasonPhrase);
                }
            }

            return eventTypes;
        }

        /// <summary>
        /// Method to check if an event exist following an id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        private bool EventExists(int id)
        {
          return _context.Events.Any(e => e.EventId == id);
        }
    }
}
