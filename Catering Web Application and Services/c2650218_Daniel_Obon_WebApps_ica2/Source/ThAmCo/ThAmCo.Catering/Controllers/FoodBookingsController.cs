using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using ThAmCo.Catering.Data;
using ThAmCo.Catering.Domain;
using ThAmCo.Catering.DTO;

namespace ThAmCo.Catering.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FoodBookingsController : ControllerBase
    {
        private readonly CateringDbContext _context;

        public FoodBookingsController(CateringDbContext context)
        {
            _context = context;
        }

        // GET: api/FoodBookings
        /// <summary>
        /// Returns/gets all food bookings
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<ActionResult<IEnumerable<FoodBookingDTO>>> GetFoodBookings()
        {
            if (_context.FoodItems == null)
            {
                return NotFound();
            }

            try
            {
                var foodbookings = await _context.FoodBookings.ToListAsync();
                List<FoodBookingDTO> dto = foodbookings.Select(fb => new DTO.FoodBookingDTO
                {
                    FoodBookingId = fb.FoodBookingId,
                    ClientReferenceId = fb.ClientReferenceId,
                    NumberOfGuest = fb.NumberOfGuest,
                    MenuId = fb.MenuId,
                }).ToList();
                return Ok(dto);
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "Unable to provide Food Bookings at this time");
            }
        }

        // GET: api/FoodBookings/5
        /// <summary>
        /// Returns/gets a particular food booking following a particular id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet("{id}")]
        public async Task<ActionResult<FoodBookingDTO>> GetFoodBooking(int id)
        {
            if (_context.FoodBookings == null)
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            var foodbookings = await _context.FoodBookings.FindAsync(id); ;

            if (foodbookings == null)
            {
                return NotFound();
            }

            var dto = new FoodBookingDTO
            {
                FoodBookingId = foodbookings.FoodBookingId,
                ClientReferenceId = foodbookings.ClientReferenceId,
                NumberOfGuest = foodbookings.NumberOfGuest,
                MenuId = foodbookings.MenuId,
            };
            return dto;
        }

        // PUT: api/FoodBookings/5
        /// <summary>
        /// Updates a food booking following a particular id
        /// </summary>
        /// <param name="id"></param>
        /// <param name="foodBookingDTO"></param>
        /// <returns></returns>
        [HttpPut("{id}")]
        public async Task<IActionResult> PutFoodBooking(int id, FoodBookingDTO foodBookingDTO)
        {
            if (id != foodBookingDTO.FoodBookingId)
            {
                return BadRequest();
            }

            var Dbfoodbooking = await _context.FoodBookings.FindAsync(id);

            if (Dbfoodbooking == null)
            {
                return NotFound();
            }

            Dbfoodbooking.FoodBookingId = foodBookingDTO.FoodBookingId;
            Dbfoodbooking.ClientReferenceId = foodBookingDTO.ClientReferenceId;
            Dbfoodbooking.NumberOfGuest = foodBookingDTO.NumberOfGuest;
            Dbfoodbooking.MenuId = foodBookingDTO.MenuId;

            _context.Entry(Dbfoodbooking).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!FoodBookingExists(id))
                {
                    return NotFound();
                }
                else
                {
                    return StatusCode(StatusCodes.Status500InternalServerError);
                }
            }

            return NoContent();
        }
    

        // POST: api/FoodBookings
        /// <summary>
        /// Creates a new food booking
        /// </summary>
        /// <param name="foodBookingDTO"></param>
        /// <returns></returns>
        [HttpPost]
        public async Task<ActionResult<FoodBookingDTO>> PostFoodBooking(FoodBookingDTO foodBookingDTO)
        {
            if (_context.FoodBookings == null)
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            if (FoodBookingClash(foodBookingDTO))
            {
                return BadRequest();
            }

            FoodBooking newFoodBooking = new FoodBooking
            {
                FoodBookingId = foodBookingDTO.FoodBookingId,
                ClientReferenceId = foodBookingDTO.ClientReferenceId,
                NumberOfGuest = foodBookingDTO.NumberOfGuest,
                MenuId = foodBookingDTO.MenuId,

            };

            try
            {
                _context.FoodBookings.Add(newFoodBooking);
                await _context.SaveChangesAsync();
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            return CreatedAtAction("GetFoodBooking", new { id = newFoodBooking.FoodBookingId }, foodBookingDTO);
        }

        // DELETE: api/FoodBookings/5
        /// <summary>
        /// Deletes a food booking following a particular id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteFoodBooking(int id)
        {
            try
            {
                var foodbooking = await _context.FoodBookings.FindAsync(id);
                if (foodbooking == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound);
                }
                _context.FoodBookings.Remove(foodbooking);
                await _context.SaveChangesAsync();
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            return StatusCode(StatusCodes.Status204NoContent);
        }

        /// <summary>
        /// Method to check if a food booking exist following a particular id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        private bool FoodBookingExists(int id)
        {
            return _context.FoodBookings.Any(e => e.FoodBookingId == id);
        }

        /// <summary>
        /// Method to check a food booking already exist (checks for if its a duplicate)
        /// </summary>
        /// <param name="foodbooking"></param>
        /// <returns></returns>
        private bool FoodBookingClash(FoodBookingDTO foodbooking)
        {
            return _context.FoodBookings.Any(e => e.FoodBookingId == foodbooking.FoodBookingId);
        }
    }
}
