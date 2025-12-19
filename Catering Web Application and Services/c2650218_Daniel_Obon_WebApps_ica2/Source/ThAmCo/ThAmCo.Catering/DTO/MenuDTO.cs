using System.ComponentModel.DataAnnotations;
using ThAmCo.Catering.Domain;

namespace ThAmCo.Catering.DTO
{
    /// <summary>
    /// Used to only return MenuId and MenuName
    /// </summary>
    public class MenuDTO
    {
        public int MenuId { get; set; }

        [Required]
        [MaxLength(50)]
        public string MenuName { get; set; } = null!;

    }

    /// <summary>
    /// Similar to MenuDTO but also includes a single FoodItem
    /// </summary>
    public class MenuFoodItemDTOs
    {
        public int MenuId { get; set; }

        [Required]
        [MaxLength(50)]
        public string MenuName { get; set; } = null!;

        public DTO.FoodItemDTO FoodItem { get; set; }
    }

    /// <summary>
    /// Similar to MenuDTO but also includes a list of FoodItems instead of just one like the previous
    /// </summary>
    public class MenuFoodItemDTO
    {
        public int MenuId { get; set;}

        [Required]
        [MaxLength(50)]
        public string MenuName { get; set; } = null!;

        public List<DTO.FoodItemDTO> FoodItems { get; set; }

        static public MenuFoodItemDTO buildDTO(Menu menu)
        {
            List<DTO.FoodItemDTO> foodItems = new List<FoodItemDTO>();
            MenuFoodItemDTO dto = new MenuFoodItemDTO();
            foodItems = menu.MenuFoodItems.Select(fi => new FoodItemDTO
            {
                FoodItemId = fi.FoodItemId,
                Description = fi.FoodItem.Description,
                UnitPrice = fi.FoodItem.UnitPrice
            }).ToList();
            if (foodItems.Count > 0)
            {
                dto.MenuId = menu.MenuId;
                dto.MenuName = menu.MenuName;
                dto.FoodItems = foodItems;
                return dto;
            }
            return null;
        }
    }
}
