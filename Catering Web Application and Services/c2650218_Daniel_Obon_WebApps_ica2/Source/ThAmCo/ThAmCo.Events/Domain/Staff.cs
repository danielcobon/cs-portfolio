using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.Domain
{
    public class Staff
    {
        [Key]
        [Display(Name = "Staff ID")]
        public int StaffId { get; set; }

        [Required]
        [MaxLength(50)]
        [Display(Name = "Staff Name")]
        public string StaffName { get; set; } = null!;

        [Required]
        [Display(Name = "Phone Number")]
        public string StaffPhone { get; set; }

        [Display(Name = "Email Address")]
        public string StaffEmail { get; set; }

        [Required]
        [Display(Name = "Staff Position")]
        public string StaffPost {  get; set; }

        // Navigation property to Staffing (many-side of one-to-many)
        public List<Staffing>Staffings { get; set; }
    }
}
