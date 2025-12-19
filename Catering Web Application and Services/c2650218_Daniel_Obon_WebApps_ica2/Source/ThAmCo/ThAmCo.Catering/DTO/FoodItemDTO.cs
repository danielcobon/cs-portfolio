using System.ComponentModel.DataAnnotations;
using ThAmCo.Catering.Domain;

namespace ThAmCo.Catering.DTO
{
    /// <summary>
    /// Used to return FoodItemId, Description and UnitPrice
    /// </summary>
    public class FoodItemDTO
    {
        public int FoodItemId { get; set; }

        [MaxLength(50)]
        public string Description { get; set; } = null!;

        public float UnitPrice { get; set; }
    }
}
