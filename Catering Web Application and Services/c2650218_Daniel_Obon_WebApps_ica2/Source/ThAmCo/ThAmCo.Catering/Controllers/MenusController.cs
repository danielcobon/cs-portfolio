using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using NuGet.DependencyResolver;
using ThAmCo.Catering.Data;
using ThAmCo.Catering.Domain;
using ThAmCo.Catering.DTO;

namespace ThAmCo.Catering.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MenusController : ControllerBase
    {
        private readonly CateringDbContext _context;

        public MenusController(CateringDbContext context)
        {
            _context = context;
        }

        // GET: api/Menus
        /// <summary>
        /// Returns a List of MenuItemDTO
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<ActionResult<IEnumerable<DTO.MenuDTO>>> GetMenus()
        {
            // Runs if List can be found
            try
            {
                var menus = await _context.Menus.ToListAsync();
                List<DTO.MenuDTO> dto = menus.Select(c => new DTO.MenuDTO
                {
                    MenuId = c.MenuId,
                    MenuName = c.MenuName,
                }
                ).ToList();
                return Ok(dto);
            }
            // Runs if List cannot be found and returns a 500 error (Server Error)
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "Unable to Provide Data at this time");
            }
        }

        // GET: api/Menus/5
        /// <summary>
        /// Returns/Gets a particular MenuItemDTO according to the id provided
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet("{id}")]
        public async Task<ActionResult<MenuDTO>> GetMenu(int id)
        {
            if(_context.Menus == null)
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            var menu = await _context.Menus.FindAsync(id);

            if (menu == null)
            {
                return NotFound();
            }

            var dto = new MenuDTO
            {
                MenuId = menu.MenuId,
                MenuName = menu.MenuName,
            };

            return dto;
        }

        // PUT: api/Menus/5
        /// <summary>
        /// Updates a particular menu with a new menu for a particular id
        /// </summary>
        /// <param name="id"></param>
        /// <param name="menu"></param>
        /// <returns></returns>        
        [HttpPut("{id}")]
        public async Task<IActionResult> PutMenu(int id, Menu menu)
        {
            if (id != menu.MenuId)
            {
                return BadRequest();
            }

            _context.Entry(menu).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!MenuExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Menus
        /// <summary>
        /// Creates a menu following menuDTO
        /// </summary>
        /// <param name="menuDTO"></param>
        /// <returns></returns>
        [HttpPost]
        public async Task<ActionResult<MenuDTO>> PostMenu(MenuDTO menuDTO)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest("Debug");
                }

                if (MenuExists(menuDTO.MenuId))
                {
                    return StatusCode(StatusCodes.Status409Conflict, $"Menu ID: {menuDTO.MenuId} already exist...");
                }

                Menu newMenu = new Menu()
                {
                    MenuId = menuDTO.MenuId,
                    MenuName = menuDTO.MenuName,
                };

                _context.Menus.Add(newMenu);
                await _context.SaveChangesAsync();
                return CreatedAtAction("GetMenu", new { id = newMenu.MenuId }, menuDTO);
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "A server side error has occured...");
            }
        }

        // DELETE: api/Menus/5
        /// <summary>
        /// Deletes a menu with a particular id
        /// </summary>
        /// <param name="menuId"></param>
        /// <returns></returns>
        [HttpDelete("{menuId}")]
        public async Task<IActionResult> DeleteMenus(int menuId)
        {
            try
            {
                var menu = await _context.FoodItems.FindAsync(menuId);
                if (menu == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound);
                }
                _context.FoodItems.Remove(menu);
                await _context.SaveChangesAsync();
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            return StatusCode(StatusCodes.Status204NoContent);
        }

        // GET: api/Menus/5
        /// <summary>
        /// Returns a specific MenuItem
        /// Returns/Gets a particular Menu with its menu food items listed
        /// </summary>
        /// <param name="menuId"></param>
        /// <returns></returns>
        [HttpGet("{menuId}/FoodItems")]
        public async Task<ActionResult<MenuDTO>> GetMenuItems(int menuId)
        {
            Menu menu = null;
            try
            {
                menu = await GetMenuFoodItems(menuId);
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }

            if (menu != null)
            {
                var dto = MenuFoodItemDTO.buildDTO(menu);
                if (dto != null)
                {
                    return Ok(dto);
                }
                else
                {
                    return StatusCode(StatusCodes.Status204NoContent);
                }
            }
            else
            {
                return StatusCode(StatusCodes.Status404NotFound);
            }
        }

        // GET: api/Menu/5/5
        /// <summary>
        /// Returns/gets a particular menu and also a particular menu food item
        /// </summary>
        /// <param name="menuId"></param>
        /// <param name="foodItemId"></param>
        /// <returns></returns>
        [HttpGet("{menuId}/{foodItemId}")]
        public async Task<ActionResult<MenuDTO>> GetFoodItem(int menuId, int foodItemId)
        {            
            try
            {
                Menu menu = await _context.Menus.FindAsync(menuId);
                if (menu == null)
                {
                    return NotFound($"ID: {menuId} in Menu does not exist...");
                }


                MenuFoodItem menuFoodItem = await _context.MenuFoodItems
                    .Include(mfi => mfi.FoodItem)
                    .FirstOrDefaultAsync(mfi => menuId == mfi.MenuId &&  foodItemId == mfi.FoodItemId);

                FoodItem foodItem = menuFoodItem.FoodItem;

                FoodItemDTO foodItemDTO = new FoodItemDTO()
                {
                    FoodItemId = foodItem.FoodItemId,
                    Description = foodItem.Description,
                    UnitPrice = foodItem.UnitPrice,
                };

                MenuFoodItemDTOs menuFoodItemDTO = new MenuFoodItemDTOs()
                {
                    MenuId = menu.MenuId,
                    MenuName = menu.MenuName,
                    FoodItem = foodItemDTO,
                };

                return Ok(menuFoodItemDTO);
            }
            catch 
            {
                return StatusCode(StatusCodes.Status500InternalServerError);
            }
        }

        // POST: api/Menus/5/5
        /// <summary>
        /// Creates a new Menu food item following a particular menu id
        /// </summary>
        /// <param name="menuId"></param>
        /// <param name="foodItemId"></param>
        /// <returns></returns>
        [HttpPost("{menuId}/{foodItemId}")]
        public async Task<ActionResult<MenuFoodItemDTO>> PostMenuItem(int menuId, int foodItemId)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return StatusCode(StatusCodes.Status400BadRequest);
                }

                Menu menu = await _context.Menus.FindAsync(menuId);
                if(menu == null)
                {
                    return NotFound($"Menu ID: {menuId} cannot be found...");
                }

                FoodItem foodItem = await _context.FoodItems.FindAsync(foodItemId);
                if(foodItem == null)
                {
                    return NotFound($"Food Item ID: {foodItemId} cannot be found...");
                }

                bool menuFoodItemExist = await _context.MenuFoodItems.AnyAsync(mfi => mfi.MenuId == menuId && mfi.FoodItemId == foodItemId);

                if(menuFoodItemExist)
                {
                    return Conflict($"Food Item ID: {foodItemId} already exist...");
                }

                MenuFoodItem newMenuFoodItem = new MenuFoodItem()
                {
                    MenuId = menuId,
                    Menu = menu,
                    FoodItemId = foodItemId,
                    FoodItem = foodItem,
                };

                _context.MenuFoodItems.Add(newMenuFoodItem);
                menu.MenuFoodItems.Add(newMenuFoodItem);
                await _context.SaveChangesAsync();

                MenuFoodItemDTOs newMenuFoodItemDTO = new MenuFoodItemDTOs()
                {
                    MenuId = menu.MenuId,
                    MenuName = menu.MenuName,
                    FoodItem = new FoodItemDTO
                    {
                        FoodItemId = foodItem.FoodItemId,
                        Description = foodItem.Description,
                        UnitPrice = foodItem.UnitPrice
                    }
                };

                return CreatedAtAction("GetFoodItem", new { menuId = menu.MenuId, foodId = foodItem.FoodItemId }, newMenuFoodItemDTO);
            }
            catch
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "Something went wrong on the server side...");
            }
        }

        // DELETE: api/Menus/5/5
        /// <summary>
        /// Deletes a particular food item following a particular menu
        /// </summary>
        /// <param name="menuId"></param>
        /// <param name="foodItemId"></param>
        /// <returns></returns>
        [HttpDelete("{menuId}/{foodItemId}")]
        public async Task<IActionResult> DeleteMenuFoodItem(int menuId, int foodItemId)
        {
            Menu menu = await _context.Menus.FindAsync(menuId);
            if (menu == null)
            {
                return NotFound($"Menu ID: {menuId} cannot found....");
            }

            MenuFoodItem menuFoodItem = await _context.MenuFoodItems.FindAsync(menuId, foodItemId);
            if(menuFoodItem == null)
            {
                return NotFound($"Food Item ID: {foodItemId} cannot found....");
            }

            _context.MenuFoodItems.Remove(menuFoodItem);
            await _context.SaveChangesAsync();

            return StatusCode(StatusCodes.Status204NoContent);
        }

        /// <summary>
        /// Method to check if a menu exist
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        private bool MenuExists(int id)
        {
            return _context.Menus.Any(e => e.MenuId == id);
        }

        /// <summary>
        /// Used to get Menu food items of a particular id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        private async Task<Menu> GetMenuFoodItems(int id)
        {
            return await _context.Menus.Include(fi => fi.MenuFoodItems).ThenInclude(fi => fi.FoodItem).SingleOrDefaultAsync(m => m.MenuId == id);
        }
    }
}
