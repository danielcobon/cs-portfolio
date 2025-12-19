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
    public class FoodItemsController : ControllerBase
    {
        private readonly CateringDbContext _context;

        public FoodItemsController(CateringDbContext context)
        {
            _context = context;
        }

        // GET: api/FoodItems
        /// <summary>
        /// Gets all food items
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<ActionResult<IEnumerable<DTO.FoodItemDTO>>> GetFoodItems()
        {
            if (_context.FoodItems == null)
            {
                return NotFound();
            }

            try
            {
                var fooditems = await _context.FoodItems.ToListAsync();
                List<FoodItemDTO> dto = fooditems.Select(fi => new DTO.FoodItemDTO
                {
                    FoodItemId = fi.FoodItemId,
                    Description = fi.Description,
                    UnitPrice = fi.UnitPrice,
                }).ToList();
                return Ok(dto);
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "Unable to provide Food Items at this time");
            }
            
        }

        // GET: api/FoodItems/5
        /// <summary>
        /// Gets a food item of a particular id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet("{id}")]
        public async Task<ActionResult<FoodItemDTO>> GetFoodItem(int id)
        {
            if(_context.FoodItems == null)
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            var fooditems = await _context.FoodItems.FindAsync(id); ;

            if(fooditems == null)
            {
                return NotFound();
            }

            var dto = new FoodItemDTO
            {
                FoodItemId = fooditems.FoodItemId,
                Description = fooditems.Description,
                UnitPrice = fooditems.UnitPrice,
            };
            return dto;
        }

        // PUT: api/FoodItems/5
        /// <summary>
        /// Updates a particular food item following a particular id
        /// </summary>
        /// <param name="id"></param>
        /// <param name="foodItemDTO"></param>
        /// <returns></returns>
        [HttpPut("{id}")]
        public async Task<IActionResult> PutFoodItem(int id, FoodItemDTO foodItemDTO)
        {
            if (id != foodItemDTO.FoodItemId)
            {
                return BadRequest();
            }

            var Dbfooditem = await _context.FoodItems.FindAsync(id);

            if (Dbfooditem == null)
            {
                return NotFound();
            }

            Dbfooditem.FoodItemId = foodItemDTO.FoodItemId;
            Dbfooditem.Description = foodItemDTO.Description;
            Dbfooditem.UnitPrice = foodItemDTO.UnitPrice;

            _context.Entry(Dbfooditem).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!FoodItemExists(id))
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

        // POST: api/FoodItems
        /// <summary>
        /// Creates a new food item
        /// </summary>
        /// <param name="foodItemDTO"></param>
        /// <returns></returns>
        [HttpPost]
        public async Task<ActionResult<FoodItemDTO>> PostFoodItem(FoodItemDTO foodItemDTO)
        {
           if(_context.FoodItems == null)
           {
                return StatusCode(StatusCodes.Status500InternalServerError);
           }

           if (FoodItemClash(foodItemDTO))
           {
                return BadRequest();
           }

            FoodItem newFoodItem = new FoodItem
            {
                FoodItemId = foodItemDTO.FoodItemId,
                Description = foodItemDTO.Description,
                UnitPrice = foodItemDTO.UnitPrice,

            };

            try
            {
                _context.FoodItems.Add(newFoodItem);
                await _context.SaveChangesAsync();
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            return CreatedAtAction("GetFoodItem", new { id = newFoodItem.FoodItemId }, foodItemDTO);
        }

        // DELETE: api/FoodItems/5
        /// <summary>
        /// Deletes a food item following a particular id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteFoodItem(int id)
        {
            //var foodItem = await _context.FoodItems.FindAsync(id);
            //if (foodItem == null)
            //{
            //    return NotFound();
            //}

            //_context.FoodItems.Remove(foodItem);
            //await _context.SaveChangesAsync();

            //return NoContent();

            try
            {
                var fooditem = await _context.FoodItems.FindAsync(id);
                if (fooditem == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound);
                }
                _context.FoodItems.Remove(fooditem);
                await _context.SaveChangesAsync();
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            return StatusCode(StatusCodes.Status204NoContent);
        }

        /// <summary>
        /// Method to check whether a particular food item exits following an id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        private bool FoodItemExists(int id)
        {
            return _context.FoodItems.Any(e => e.FoodItemId == id);
        }

        /// <summary>
        /// Method to check a food item already exist (checks for if its a duplicate)
        /// </summary>
        /// <param name="fooditem"></param>
        /// <returns></returns>
        private bool FoodItemClash(FoodItemDTO fooditem)
        {
            return _context.FoodItems.Any(e => e.FoodItemId == fooditem.FoodItemId);
        }
    }
}
