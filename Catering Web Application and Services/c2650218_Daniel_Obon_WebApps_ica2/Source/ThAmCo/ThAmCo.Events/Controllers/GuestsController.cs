using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using ThAmCo.Events.Data;
using ThAmCo.Events.Domain;
using ThAmCo.Events.Models;

namespace ThAmCo.Events.Controllers
{
    public class GuestsController : Controller
    {
        private readonly EventsDbContext _context;

        public GuestsController(EventsDbContext context)
        {
            _context = context;
        }

        // GET: Guests
        public async Task<IActionResult> Index()
        {
              return View(await _context.Guests.ToListAsync());
        }

        // GET: Guests/Details/5
        /// <summary>
        /// Returns/gets the detail of a guest following an id in Details
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.Guests == null)
            {
                return NotFound();
            }

            var guest = await _context.Guests
                .FirstOrDefaultAsync(m => m.GuestId == id);

            if (guest == null)
            {
                return NotFound();
            }

            GuestDetailsViewModel guestDetails = new GuestDetailsViewModel()
            {
                GuestId = guest.GuestId,
                GuestName = guest.GuestName,
                GuestPhone = guest.GuestPhone,
                GuestEmail = guest.GuestEmail,

            };

            return View(guestDetails);
        }

        // GET: Guests/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: Guests/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        /// <summary>
        /// Creates a guest in Create
        /// </summary>
        /// <param name="guest"></param>
        /// <returns></returns>
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("GuestId,GuestName,GuestPhone,GuestEmail,Attendance")] Guest guest)
        {
            if (ModelState.IsValid)
            {
                _context.Add(guest);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(guest);
        }

        // GET: Guests/Edit/5
        /// <summary>
        /// Returns/gets a guest of a particular id in Edit
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.Guests == null)
            {
                return NotFound();
            }

            var guest = await _context.Guests.FindAsync(id);
            if (guest == null)
            {
                return NotFound();
            }

            EditGuestViewModel guestEdit = new EditGuestViewModel()
            {
                GuestId = guest.GuestId,
                GuestName = guest.GuestName,
                GuestPhone = guest.GuestPhone,
                GuestEmail = guest.GuestEmail,
                Attendance = (Models.AttendanceStatus)guest.Attendance

            };
            return View(guestEdit);
        }

        // POST: Guests/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        /// <summary>
        /// Creates a new guest in Edit
        /// </summary>
        /// <param name="id"></param>
        /// <param name="guest"></param>
        /// <returns></returns>
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("GuestId,GuestName,GuestPhone,GuestEmail,Attendance")] Guest guest)
        {
            if (id != guest.GuestId)
            {
                return NotFound();
            }
            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(guest);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!GuestExists(guest.GuestId))
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
            return View(guest);
        }

        // GET: Guests/Delete/5
        /// <summary>
        /// Returns/gets a guest following an id in Delete
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.Guests == null)
            {
                return NotFound();
            }

            var guest = await _context.Guests
                .FirstOrDefaultAsync(m => m.GuestId == id);
            if (guest == null)
            {
                return NotFound();
            }

            return View(guest);
        }

        // POST: Guests/Delete/5
        /// <summary>
        /// Deletes a guest following an id in Delete
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.Guests == null)
            {
                return Problem("Entity set 'EventsDbContext.Guests'  is null.");
            }
            var guest = await _context.Guests.FindAsync(id);
            if (guest != null)
            {
                _context.Guests.Remove(guest);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        /// <summary>
        /// Method to check if a guest exist following an id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        private bool GuestExists(int id)
        {
          return _context.Guests.Any(e => e.GuestId == id);
        }
    }
}
