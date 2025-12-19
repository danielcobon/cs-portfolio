using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.Models
{
    public class StaffDetailsViewModel
    {
        [Display(Name = "Staff ID")]
        public int StaffId { get; set; }

        [Display(Name = "Staff Name")]
        public string StaffName { get; set; }

        [Display(Name = "Position")]
        public string StaffPost {  get; set; }
    }
}
