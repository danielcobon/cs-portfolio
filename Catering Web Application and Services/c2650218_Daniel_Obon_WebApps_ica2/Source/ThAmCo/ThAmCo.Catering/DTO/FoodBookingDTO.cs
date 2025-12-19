using System.ComponentModel.DataAnnotations;
using ThAmCo.Catering.Domain;

namespace ThAmCo.Catering.DTO
{
    /// <summary>
    /// Returns FoodBookingID, ClientReferenceId, NumberOfGuest and MenuId
    /// </summary>
    public class FoodBookingDTO
    {
        [Key]
        public int FoodBookingId { get; set; }

        public int ClientReferenceId { get; set; }

        public int NumberOfGuest { get; set; }

        public int MenuId { get; set; }
    }
}
