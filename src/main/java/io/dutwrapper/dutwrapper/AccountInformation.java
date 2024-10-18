package io.dutwrapper.dutwrapper;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class AccountInformation {
    public static class WeekAffected implements Serializable {
        @SerializedName("start")
        private @Nonnull Integer start;
        @SerializedName("end")
        private @Nonnull Integer end;

        public WeekAffected(@Nonnull Integer start, @Nonnull Integer end) {
            this.start = start;
            this.end = end;
        }

        public @Nonnull Integer getStart() {
            return start;
        }

        public void setStart(@Nonnull Integer start) {
            this.start = start;
        }

        public @Nonnull Integer getEnd() {
            return end;
        }

        public void setEnd(@Nonnull Integer end) {
            this.end = end;
        }
    }

    // Details in http://daotao.dut.udn.vn/download2/Guide_Dangkyhoc.pdf, page 28
    public static class SubjectCode implements Serializable {
        // Area 1
        @SerializedName("subject_id")
        private @Nullable String subjectId;
        // Area 2
        @SerializedName("school_year_id")
        private @Nullable String schoolYearId;
        // Area 3
        @SerializedName("student_year_id")
        private String studentYearId;
        // Area 4
        @SerializedName("class_id")
        private String classId;

        public SubjectCode(String studentYearId, String classId) {
            this.subjectId = null;
            this.schoolYearId = null;
            this.studentYearId = studentYearId;
            this.classId = classId;
        }

        public SubjectCode(@Nullable String subjectId, @Nullable String schoolYearId, String studentYearId, String classId) {
            this.subjectId = subjectId;
            this.schoolYearId = schoolYearId;
            this.studentYearId = studentYearId;
            this.classId = classId;
        }

        public SubjectCode(String input) {
            if (input.split("\\.").length == 4) {
                this.subjectId = input.split("\\.")[0];
                this.schoolYearId = input.split("\\.")[1];
                this.studentYearId = input.split("\\.")[2];
                this.classId = input.split("\\.")[3];
            } else if (input.split("\\.").length == 2) {
                this.subjectId = null;
                this.schoolYearId = null;
                this.studentYearId = input.split("\\.")[0];
                this.classId = input.split("\\.")[1];
            } else {
                this.subjectId = null;
                this.schoolYearId = null;
                this.studentYearId = "-";
                this.classId = "-";
            }
        }

        public @Nullable String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(@Nullable String subjectId) {
            this.subjectId = subjectId;
        }

        public @Nullable String getSchoolYearId() {
            return schoolYearId;
        }

        public void setSchoolYearId(@Nullable String schoolYearId) {
            this.schoolYearId = schoolYearId;
        }

        public String getStudentYearId() {
            return studentYearId;
        }

        public void setStudentYearId(String studentYearId) {
            this.studentYearId = studentYearId;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        @Override
        public String toString() {
            return toString(subjectId == null || schoolYearId == null);
        }

        public String toString(Boolean twoLastDigitOnly) {
            if (!twoLastDigitOnly && subjectId != null && schoolYearId != null) {
                return String.format("%s.%s.%s.%s", subjectId, schoolYearId, studentYearId, classId);
            } else {
                return String.format("%s.%s", studentYearId, classId);
            }
        }

        public Boolean equalsTwoDigits(SubjectCode codeItem) {
            return Objects.equals(this.studentYearId, codeItem.studentYearId) &&
                    Objects.equals(this.classId, codeItem.classId);
        }

        public Boolean equals(SubjectCode codeItem) {
            return Objects.equals(this.subjectId, codeItem.subjectId) &&
                    Objects.equals(this.studentYearId, codeItem.studentYearId) &&
                    this.equalsTwoDigits(codeItem);
        }
    }

    public static class LessonAffected implements Serializable {
        @SerializedName("start")
        private Integer start;
        @SerializedName("end")
        private Integer end;

        public LessonAffected(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        @Override
        public String toString() {
            return String.format("%d-%d", start, end);
        }
    }

    public static class ScheduleItem implements Serializable {
        @SerializedName("day_of_week")
        private Integer dayOfWeek;
        @SerializedName("lesson_affected")
        private LessonAffected lesson;
        @SerializedName("room")
        private String room;

        public ScheduleItem() {

        }

        public ScheduleItem(Integer dayOfWeek, LessonAffected lesson, String room) {
            this.dayOfWeek = dayOfWeek;
            this.room = room;
            this.lesson = lesson;
        }

        public Integer getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public LessonAffected getLesson() {
            return lesson;
        }

        public void setLesson(LessonAffected lesson) {
            this.lesson = lesson;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

    }

    public static class ScheduleStudy implements Serializable {
        @SerializedName("schedule_list")
        private ArrayList<ScheduleItem> scheduleList;
        @SerializedName("week_affected")
        private ArrayList<WeekAffected> weekAffected;

        public ScheduleStudy() {
            scheduleList = new ArrayList<>();
            weekAffected = new ArrayList<>();
        }

        public ScheduleStudy(ArrayList<ScheduleItem> scheduleList, ArrayList<WeekAffected> weekAffected) {
            this.scheduleList = scheduleList;
            this.weekAffected = weekAffected;
        }

        public ArrayList<ScheduleItem> getScheduleList() {
            return scheduleList;
        }

        public void setScheduleList(ArrayList<ScheduleItem> scheduleList) {
            this.scheduleList = scheduleList;
        }

        public ArrayList<WeekAffected> getWeekAffected() {
            return weekAffected;
        }

        public void setWeekAffected(ArrayList<WeekAffected> weekAffected) {
            this.weekAffected = weekAffected;
        }
    }

    public static class ScheduleExam implements Serializable {
        @SerializedName("group")
        private String group;
        @SerializedName("is_global")
        private Boolean isGlobal;
        @SerializedName("date")
        private Long date;
        @SerializedName("room")
        private String room;

        public ScheduleExam() {
            this.group = "";
            this.isGlobal = false;
            this.date = 0L;
            this.room = "";
        }

        public ScheduleExam(String group, Boolean isGlobal, Long date, String room) {
            this.group = group;
            this.isGlobal = isGlobal;
            this.date = date;
            this.room = room;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public Boolean getIsGlobal() {
            return isGlobal;
        }

        public void setIsGlobal(Boolean isGlobal) {
            this.isGlobal = isGlobal;
        }

        public Long getDate() {
            return date;
        }

        public void setDate(Long date) {
            this.date = date;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }

    public static class SubjectInformation implements Serializable {
        @SerializedName("id")
        private SubjectCode id;
        @SerializedName("name")
        private String name;
        @SerializedName("credit")
        private Integer credit;
        @SerializedName("is_high_quality")
        private Boolean isHighQuality;
        @SerializedName("lecturer")
        private String lecturer;
        @SerializedName("schedule_study")
        private ScheduleStudy scheduleStudy;
        @SerializedName("schedule_exam")
        private ScheduleExam scheduleExam;
        @SerializedName("point_formula")
        private String pointFormula;

        public SubjectInformation() {

        }

        public SubjectInformation(SubjectCode id, String name, Integer credit, Boolean isHighQuality,
                                  ScheduleStudy scheduleStudy, ScheduleExam subjectExam) {
            this.id = id;
            this.name = name;
            this.credit = credit;
            this.isHighQuality = isHighQuality;
            this.scheduleStudy = scheduleStudy;
            this.scheduleExam = subjectExam;
        }

        public SubjectCode getId() {
            return id;
        }

        public void setId(SubjectCode id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCredit() {
            return credit;
        }

        public void setCredit(Integer credit) {
            this.credit = credit;
        }

        public Boolean getIsHighQuality() {
            return isHighQuality;
        }

        public void setIsHighQuality(Boolean isHighQuality) {
            this.isHighQuality = isHighQuality;
        }

        public String getLecturer() {
            return lecturer;
        }

        public void setLecturer(String lecturer) {
            this.lecturer = lecturer;
        }

        public ScheduleStudy getScheduleStudy() {
            return scheduleStudy;
        }

        public void setScheduleStudy(ScheduleStudy scheduleStudy) {
            this.scheduleStudy = scheduleStudy;
        }

        public ScheduleExam getScheduleExam() {
            return scheduleExam;
        }

        public void setScheduleExam(ScheduleExam scheduleExam) {
            this.scheduleExam = scheduleExam;
        }

        public String getPointFormula() {
            return pointFormula;
        }

        public void setPointFormula(String pointFormula) {
            this.pointFormula = pointFormula;
        }
    }

    public static class SubjectFee implements Serializable {
        @SerializedName("id")
        private SubjectCode id;
        @SerializedName("name")
        private String name;
        @SerializedName("credit")
        private Integer credit;
        @SerializedName("is_high_quality")
        private Boolean isHighQuality;
        @SerializedName("price")
        private Double price;
        @SerializedName("is_debt")
        private Boolean debt;
        @SerializedName("is_restudy")
        private Boolean isRestudy;
        @SerializedName("verified_payment_at")
        private String verifiedPaymentAt;

        public SubjectFee() {

        }

        public SubjectFee(SubjectCode id, String name, Integer credit, Boolean isHighQuality, Double price,
                          Boolean debt,
                          Boolean isRestudy, String verifiedPaymentAt) {
            this.id = id;
            this.name = name;
            this.credit = credit;
            this.isHighQuality = isHighQuality;
            this.price = price;
            this.debt = debt;
            this.isRestudy = isRestudy;
            this.verifiedPaymentAt = verifiedPaymentAt;
        }

        public SubjectCode getId() {
            return id;
        }

        public void setId(SubjectCode id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCredit() {
            return credit;
        }

        public void setCredit(Integer credit) {
            this.credit = credit;
        }

        public Boolean getIsHighQuality() {
            return isHighQuality;
        }

        public void setIsHighQuality(Boolean isHighQuality) {
            this.isHighQuality = isHighQuality;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Boolean getDebt() {
            return debt;
        }

        public void setDebt(Boolean debt) {
            this.debt = debt;
        }

        public Boolean getIsRestudy() {
            return isRestudy;
        }

        public void setIsRestudy(Boolean isRestudy) {
            this.isRestudy = isRestudy;
        }

        public String getVerifiedPaymentAt() {
            return verifiedPaymentAt;
        }

        public void setVerifiedPaymentAt(String verifiedPaymentAt) {
            this.verifiedPaymentAt = verifiedPaymentAt;
        }
    }

    public static class StudentInformation implements Serializable {
        @SerializedName("name")
        private String name;
        @SerializedName("date_of_birth")
        private String dateOfBirth;
        @SerializedName("birth_pace")
        private String birthPlace;
        @SerializedName("gender")
        private String gender;
        @SerializedName("ethnicity")
        private String ethnicity;
        @SerializedName("nationality")
        private String nationality;
        @SerializedName("national_id_card")
        private String nationalIdCard;
        @SerializedName("national_id_card_issue_date")
        private String nationalIdCardIssueDate;
        @SerializedName("national_id_card_issue_place")
        private String nationalIdCardIssuePlace;
        @SerializedName("citizen_id_card")
        private String citizenIdCard;
        @SerializedName("citizen_id_card_issue_date")
        private String citizenIdCardIssueDate;
        @SerializedName("religion")
        private String religion;
        @SerializedName("account_bank_id")
        private String accountBankId;
        @SerializedName("account_bank_name")
        private String accountBankName;
        @SerializedName("hi_id")
        private String hIId;
        @SerializedName("hi_expire_date")
        private String hIExpireDate;
        @SerializedName("specialization")
        private String specialization;
        @SerializedName("school_class")
        private String schoolClass;
        @SerializedName("training_program_plan")
        private String trainingProgramPlan;
        @SerializedName("training_program_plan_2")
        private String trainingProgramPlan2;
        @SerializedName("school_email")
        private String schoolEmail;
        @SerializedName("personal_email")
        private String personalEmail;
        @SerializedName("school_email_init_pass")
        private String schoolEmailInitPass;
        @SerializedName("facebook_url")
        private String facebookUrl;
        @SerializedName("phone_number")
        private String phoneNumber;
        @SerializedName("address")
        private String address;
        @SerializedName("address_from")
        private String addressFrom;
        @SerializedName("address_city")
        private String addressCity;
        @SerializedName("address_district")
        private String addressDistrict;
        @SerializedName("address_sub_district")
        private String addressSubDistrict;
        @SerializedName("student_id")
        private String studentId;

        public StudentInformation(String name, String dateOfBirth, String birthPlace, String gender, String ethnicity,
                                  String nationality, String nationalIdCard, String nationalIdCardIssueDate, String nationalIdCardIssuePlace,
                                  String citizenIdCard, String citizenIdCardIssueDate, String religion, String accountBankId,
                                  String accountBankName, String hIId, String hIExpireDate, String specialization, String schoolClass,
                                  String trainingProgramPlan, String trainingProgramPlan2, String schoolEmail, String personalEmail,
                                  String schoolEmailInitPass, String facebookUrl, String phoneNumber, String address, String addressFrom,
                                  String addressCity, String addressDistrict, String addressSubDistrict, String studentId) {
            this.name = name;
            this.dateOfBirth = dateOfBirth;
            this.birthPlace = birthPlace;
            this.gender = gender;
            this.ethnicity = ethnicity;
            this.nationality = nationality;
            this.nationalIdCard = nationalIdCard;
            this.nationalIdCardIssueDate = nationalIdCardIssueDate;
            this.nationalIdCardIssuePlace = nationalIdCardIssuePlace;
            this.citizenIdCard = citizenIdCard;
            this.citizenIdCardIssueDate = citizenIdCardIssueDate;
            this.religion = religion;
            this.accountBankId = accountBankId;
            this.accountBankName = accountBankName;
            this.hIId = hIId;
            this.hIExpireDate = hIExpireDate;
            this.specialization = specialization;
            this.schoolClass = schoolClass;
            this.trainingProgramPlan = trainingProgramPlan;
            this.trainingProgramPlan2 = trainingProgramPlan2;
            this.schoolEmail = schoolEmail;
            this.personalEmail = personalEmail;
            this.schoolEmailInitPass = schoolEmailInitPass;
            this.facebookUrl = facebookUrl;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.addressFrom = addressFrom;
            this.addressCity = addressCity;
            this.addressDistrict = addressDistrict;
            this.addressSubDistrict = addressSubDistrict;
            this.studentId = studentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getBirthPlace() {
            return birthPlace;
        }

        public void setBirthPlace(String birthPlace) {
            this.birthPlace = birthPlace;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEthnicity() {
            return ethnicity;
        }

        public void setEthnicity(String ethnicity) {
            this.ethnicity = ethnicity;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getNationalIdCard() {
            return nationalIdCard;
        }

        public void setNationalIdCard(String nationalIdCard) {
            this.nationalIdCard = nationalIdCard;
        }

        public String getNationalIdCardIssueDate() {
            return nationalIdCardIssueDate;
        }

        public void setNationalIdCardIssueDate(String nationalIdCardIssueDate) {
            this.nationalIdCardIssueDate = nationalIdCardIssueDate;
        }

        public String getNationalIdCardIssuePlace() {
            return nationalIdCardIssuePlace;
        }

        public void setNationalIdCardIssuePlace(String nationalIdCardIssuePlace) {
            this.nationalIdCardIssuePlace = nationalIdCardIssuePlace;
        }

        public String getCitizenIdCard() {
            return citizenIdCard;
        }

        public void setCitizenIdCard(String citizenIdCard) {
            this.citizenIdCard = citizenIdCard;
        }

        public String getCitizenIdCardIssueDate() {
            return citizenIdCardIssueDate;
        }

        public void setCitizenIdCardIssueDate(String citizenIdCardIssueDate) {
            this.citizenIdCardIssueDate = citizenIdCardIssueDate;
        }

        public String getReligion() {
            return religion;
        }

        public void setReligion(String religion) {
            this.religion = religion;
        }

        public String getAccountBankId() {
            return accountBankId;
        }

        public void setAccountBankId(String accountBankId) {
            this.accountBankId = accountBankId;
        }

        public String getAccountBankName() {
            return accountBankName;
        }

        public void setAccountBankName(String accountBankName) {
            this.accountBankName = accountBankName;
        }

        public String getHIId() {
            return hIId;
        }

        public void setHIId(String hIId) {
            this.hIId = hIId;
        }

        public String getHIExpireDate() {
            return hIExpireDate;
        }

        public void setHIExpireDate(String hIExpireDate) {
            this.hIExpireDate = hIExpireDate;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getSchoolClass() {
            return schoolClass;
        }

        public void setSchoolClass(String schoolClass) {
            this.schoolClass = schoolClass;
        }

        public String getTrainingProgramPlan() {
            return trainingProgramPlan;
        }

        public void setTrainingProgramPlan(String trainingProgramPlan) {
            this.trainingProgramPlan = trainingProgramPlan;
        }

        public String getTrainingProgramPlan2() {
            return trainingProgramPlan2;
        }

        public void setTrainingProgramPlan2(String trainingProgramPlan2) {
            this.trainingProgramPlan2 = trainingProgramPlan2;
        }

        public String getSchoolEmail() {
            return schoolEmail;
        }

        public void setSchoolEmail(String schoolEmail) {
            this.schoolEmail = schoolEmail;
        }

        public String getPersonalEmail() {
            return personalEmail;
        }

        public void setPersonalEmail(String personalEmail) {
            this.personalEmail = personalEmail;
        }

        public String getSchoolEmailInitPass() {
            return schoolEmailInitPass;
        }

        public void setSchoolEmailInitPass(String schoolEmailInitPass) {
            this.schoolEmailInitPass = schoolEmailInitPass;
        }

        public String getFacebookUrl() {
            return facebookUrl;
        }

        public void setFacebookUrl(String facebookUrl) {
            this.facebookUrl = facebookUrl;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddressFrom() {
            return addressFrom;
        }

        public void setAddressFrom(String addressFrom) {
            this.addressFrom = addressFrom;
        }

        public String getAddressCity() {
            return addressCity;
        }

        public void setAddressCity(String addressCity) {
            this.addressCity = addressCity;
        }

        public String getAddressDistrict() {
            return addressDistrict;
        }

        public void setAddressDistrict(String addressDistrict) {
            this.addressDistrict = addressDistrict;
        }

        public String getAddressSubDistrict() {
            return addressSubDistrict;
        }

        public void setAddressSubDistrict(String addressSubDistrict) {
            this.addressSubDistrict = addressSubDistrict;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }
    }

    public static class TrainingSummary implements Serializable {
        @SerializedName("school_year_start")
        private String schoolYearStart;
        @SerializedName("school_year_current")
        private String schoolYearCurrent;
        @SerializedName("credit_collected")
        private Double creditCollected;
        @SerializedName("avg_train_score_4")
        private Double avgTrainingScore4;
        @SerializedName("avg_social")
        private Double avgSocial;

        public TrainingSummary() {
        }

        public TrainingSummary(String schoolYearStart, String schoolYearCurrent, Double creditCollected,
                               Double avgTrainingScore4, Double avgSocial) {
            this.schoolYearStart = schoolYearStart;
            this.schoolYearCurrent = schoolYearCurrent;
            this.creditCollected = creditCollected;
            this.avgTrainingScore4 = avgTrainingScore4;
            this.avgSocial = avgSocial;
        }

        public String getSchoolYearStart() {
            return schoolYearStart;
        }

        public void setSchoolYearStart(String schoolYearStart) {
            this.schoolYearStart = schoolYearStart;
        }

        public String getSchoolYearCurrent() {
            return schoolYearCurrent;
        }

        public void setSchoolYearCurrent(String schoolYearCurrent) {
            this.schoolYearCurrent = schoolYearCurrent;
        }

        public Double getCreditCollected() {
            return creditCollected;
        }

        public void setCreditCollected(Double creditCollected) {
            this.creditCollected = creditCollected;
        }

        public Double getAvgTrainingScore4() {
            return avgTrainingScore4;
        }

        public void setAvgTrainingScore4(Double avgTrainingScore4) {
            this.avgTrainingScore4 = avgTrainingScore4;
        }

        public Double getAvgSocial() {
            return avgSocial;
        }

        public void setAvgSocial(Double avgSocial) {
            this.avgSocial = avgSocial;
        }
    }

    public static class GraduateStatus implements Serializable {
        @SerializedName("has_sig_physical_education")
        private Boolean hasSigPhysicalEducation;
        @SerializedName("has_sig_national_defense_education")
        private Boolean hasSigNationalDefenseEducation;
        @SerializedName("has_sig_english")
        private Boolean hasSigEnglish;
        @SerializedName("has_sig_it")
        private Boolean hasSigIT;
        @SerializedName("has_qualified_graduate")
        private Boolean hasQualifiedGraduate;

        // Rewards information
        @SerializedName("rewards_info")
        private String rewardsInfo;

        // Discipline
        @SerializedName("discipline_info")
        private String discipline;

        // Thesis Graduation status
        @SerializedName("eligible_graduation_thesis_status")
        private String eligibleGraduationThesisStatus;

        // Graduation status
        @SerializedName("eligible_graduation_status")
        private String eligibleGraduationStatus;

        public GraduateStatus(Boolean hasSigPhysicalEducation, Boolean hasSigNationalDefenseEducation, Boolean hasSigEnglish, Boolean hasSigIT,
                              Boolean hasQualifiedGraduate, String rewardsInfo, String discipline, String eligibleGraduationThesisStatus, String eligibleGraduationStatus) {
            this.hasSigPhysicalEducation = hasSigPhysicalEducation;
            this.hasSigNationalDefenseEducation = hasSigNationalDefenseEducation;
            this.hasSigEnglish = hasSigEnglish;
            this.hasSigIT = hasSigIT;
            this.hasQualifiedGraduate = hasQualifiedGraduate;
            this.rewardsInfo = rewardsInfo;
            this.discipline = discipline;
            this.eligibleGraduationThesisStatus = eligibleGraduationThesisStatus;
            this.eligibleGraduationStatus = eligibleGraduationStatus;
        }

        public Boolean getHasSigPhysicalEducation() {
            return hasSigPhysicalEducation;
        }

        public void setHasSigPhysicalEducation(Boolean hasSigPhysicalEducation) {
            this.hasSigPhysicalEducation = hasSigPhysicalEducation;
        }

        public Boolean getHasSigNationalDefenseEducation() {
            return hasSigNationalDefenseEducation;
        }

        public void setHasSigNationalDefenseEducation(Boolean hasSigNationalDefenseEducation) {
            this.hasSigNationalDefenseEducation = hasSigNationalDefenseEducation;
        }

        public Boolean getHasSigEnglish() {
            return hasSigEnglish;
        }

        public void setHasSigEnglish(Boolean hasSigEnglish) {
            this.hasSigEnglish = hasSigEnglish;
        }

        public Boolean getHasSigIT() {
            return hasSigIT;
        }

        public void setHasSigIT(Boolean hasSigIT) {
            this.hasSigIT = hasSigIT;
        }

        public Boolean getHasQualifiedGraduate() {
            return hasQualifiedGraduate;
        }

        public void setHasQualifiedGraduate(Boolean hasQualifiedGraduate) {
            this.hasQualifiedGraduate = hasQualifiedGraduate;
        }

        public String getRewardsInfo() {
            return rewardsInfo;
        }

        public void setRewardsInfo(String info1) {
            this.rewardsInfo = info1;
        }

        public String getDiscipline() {
            return discipline;
        }

        public void setDiscipline(String info2) {
            this.discipline = info2;
        }

        public String getEligibleGraduationThesisStatus() {
            return eligibleGraduationThesisStatus;
        }

        public void setEligibleGraduationThesisStatus(String info3) {
            this.eligibleGraduationThesisStatus = info3;
        }

        public String getEligibleGraduationStatus() {
            return eligibleGraduationStatus;
        }

        public void setEligibleGraduationStatus(String approveGraduateProcessInfo) {
            this.eligibleGraduationStatus = approveGraduateProcessInfo;
        }
    }

    public static class SubjectResult implements Serializable {
        @SerializedName("index")
        private Integer index;
        @SerializedName("school_year")
        private String schoolYear;
        @SerializedName("is_extended_summer")
        private Boolean isExtendedSemester;
        @SerializedName("id")
        private SubjectCode id;
        @SerializedName("name")
        private String name;
        @SerializedName("credit")
        private Double credit;
        @SerializedName("point_formula")
        private @Nullable String pointFormula;
        @SerializedName("point_bt")
        private @Nullable Double pointBT;
        @SerializedName("point_bv")
        private @Nullable Double pointBV;
        @SerializedName("point_cc")
        private @Nullable Double pointCC;
        @SerializedName("point_ck")
        private @Nullable Double pointCK;
        @SerializedName("point_gk")
        private @Nullable Double pointGK;
        @SerializedName("point_qt")
        private @Nullable Double pointQT;
        @SerializedName("point_th")
        private @Nullable Double pointTH;
        @SerializedName("point_tt")
        private @Nullable Double pointTT;
        @SerializedName("result_t4")
        private @Nullable Double resultT4;
        @SerializedName("result_t10")
        private @Nullable Double resultT10;
        @SerializedName("result_by_char")
        private @Nullable String resultByChar;
        @SerializedName("is_restudy")
        private Boolean isReStudy;

        public SubjectResult(Integer index, String schoolYear, Boolean isExtendedSemester, SubjectCode id, String name,
                             Double credit, @Nullable String pointFormula, @Nullable Double pointBT, @Nullable Double pointBV, @Nullable Double pointCC, @Nullable Double pointCK,
                             @Nullable Double pointGK, @Nullable Double pointQT, @Nullable Double pointTH, @Nullable Double pointTT, @Nullable Double resultT10, @Nullable Double resultT4, @Nullable String resultByChar,
                             Boolean isReStudy) {
            this.index = index;
            this.schoolYear = schoolYear;
            this.isExtendedSemester = isExtendedSemester;
            this.id = id;
            this.name = name;
            this.credit = credit;
            this.pointFormula = pointFormula;
            this.pointBT = pointBT;
            this.pointBV = pointBV;
            this.pointCC = pointCC;
            this.pointCK = pointCK;
            this.pointGK = pointGK;
            this.pointQT = pointQT;
            this.pointTH = pointTH;
            this.pointTT = pointTT;
            this.resultT4 = resultT4;
            this.resultT10 = resultT10;
            this.resultByChar = resultByChar;
            this.isReStudy = isReStudy != null ? isReStudy : false;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getSchoolYear() {
            return schoolYear;
        }

        public void setSchoolYear(String schoolYear) {
            this.schoolYear = schoolYear;
        }

        public Boolean getIsExtendedSemester() {
            return isExtendedSemester;
        }

        public void setIsExtendedSemester(Boolean isExtendedSemester) {
            this.isExtendedSemester = isExtendedSemester;
        }

        public SubjectCode getId() {
            return id;
        }

        public void setId(SubjectCode id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getCredit() {
            return credit;
        }

        public void setCredit(Double credit) {
            this.credit = credit;
        }

        public @Nullable String getPointFormula() {
            return pointFormula;
        }

        public void setPointFormula(@Nullable String pointFormula) {
            this.pointFormula = pointFormula;
        }

        public @Nullable Double getPointBT() {
            return pointBT;
        }

        public void setPointBT(@Nullable Double pointBT) {
            this.pointBT = pointBT;
        }

        public @Nullable Double getPointBV() {
            return pointBV;
        }

        public void setPointBV(@Nullable Double pointBV) {
            this.pointBV = pointBV;
        }

        public @Nullable Double getPointCC() {
            return pointCC;
        }

        public void setPointCC(@Nullable Double pointCC) {
            this.pointCC = pointCC;
        }

        public @Nullable Double getPointCK() {
            return pointCK;
        }

        public void setPointCK(@Nullable Double pointCK) {
            this.pointCK = pointCK;
        }

        public @Nullable Double getPointGK() {
            return pointGK;
        }

        public void setPointGK(@Nullable Double pointGK) {
            this.pointGK = pointGK;
        }

        public @Nullable Double getPointQT() {
            return pointQT;
        }

        public void setPointQT(@Nullable Double pointQT) {
            this.pointQT = pointQT;
        }

        public @Nullable Double getPointTH() {
            return pointTH;
        }

        public void setPointTH(@Nullable Double pointTH) {
            this.pointTH = pointTH;
        }

        public @Nullable Double getPointTT() {
            return pointTT;
        }

        public void setPointTT(@Nullable Double pointTT) {
            this.pointTT = pointTT;
        }

        public @Nullable Double getResultT4() {
            return resultT4;
        }

        public void setResultT4(@Nullable Double resultT4) {
            this.resultT4 = resultT4;
        }

        public @Nullable Double getResultT10() {
            return resultT10;
        }

        public void setResultT10(@Nullable Double resultT10) {
            this.resultT10 = resultT10;
        }

        public @Nullable String getResultByChar() {
            return resultByChar;
        }

        public void setResultByChar(@Nullable String resultByChar) {
            this.resultByChar = resultByChar;
        }

        public Boolean getIsReStudy() {
            return isReStudy;
        }

        public void setIsReStudy(Boolean isReStudy) {
            this.isReStudy = isReStudy;
        }
    }

    public static class TrainingStatus implements Serializable {
        @SerializedName("training_summary")
        private TrainingSummary trainingSummary;
        @SerializedName("graduate_status")
        private GraduateStatus graduateStatus;
        @SerializedName("subject_result")
        private ArrayList<SubjectResult> subjectResultList;

        public TrainingStatus() {
        }

        public TrainingStatus(TrainingSummary trainingSummary, GraduateStatus graduateStatus,
                              ArrayList<SubjectResult> subjectResultList) {
            this.trainingSummary = trainingSummary;
            this.graduateStatus = graduateStatus;
            this.subjectResultList = subjectResultList;
        }

        public TrainingSummary getTrainingSummary() {
            return trainingSummary;
        }

        public void setTrainingSummary(TrainingSummary trainingSummary) {
            this.trainingSummary = trainingSummary;
        }

        public GraduateStatus getGraduateStatus() {
            return graduateStatus;
        }

        public void setGraduateStatus(GraduateStatus graduateStatus) {
            this.graduateStatus = graduateStatus;
        }

        public ArrayList<SubjectResult> getSubjectResultList() {
            return subjectResultList;
        }

        public void setSubjectResultList(ArrayList<SubjectResult> subjectResultList) {
            this.subjectResultList = subjectResultList;
        }
    }

    public static class FormRequest implements Serializable {
        @SerializedName("id")
        private String id;
        @SerializedName("document_name")
        private String documentName;
        @SerializedName("document_additional_info")
        private String documentAdditionalInfo;
        @SerializedName("date_requested")
        private Long dateRequested;
        @SerializedName("quantity")
        private Integer quantity;
        @SerializedName("response_date_printed")
        private Long dateResponsePrinted;
        @SerializedName("response_date_receivable")
        private Long dateResponseReceivable;
        @SerializedName("response_is_received")
        private Boolean isResponseReceived;
        @SerializedName("response_additional_info")
        private String responseAdditionalInfo;

        public FormRequest(String id, String documentName, String documentAdditionalInfo, Long dateRequested, Integer quantity, Long dateResponsePrinted, Long dateResponseReceivable, Boolean isResponseReceived, String responseAdditionalInfo) {
            this.id = id;
            this.documentName = documentName;
            this.documentAdditionalInfo = documentAdditionalInfo;
            this.dateRequested = dateRequested;
            this.quantity = quantity;
            this.dateResponsePrinted = dateResponsePrinted;
            this.dateResponseReceivable = dateResponseReceivable;
            this.isResponseReceived = isResponseReceived;
            this.responseAdditionalInfo = responseAdditionalInfo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDocumentName() {
            return documentName;
        }

        public void setDocumentName(String documentName) {
            this.documentName = documentName;
        }

        public String getDocumentAdditionalInfo() {
            return documentAdditionalInfo;
        }

        public void setDocumentAdditionalInfo(String documentAdditionalInfo) {
            this.documentAdditionalInfo = documentAdditionalInfo;
        }

        public Long getDateRequested() {
            return dateRequested;
        }

        public void setDateRequested(Long dateRequested) {
            this.dateRequested = dateRequested;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Long getDateResponsePrinted() {
            return dateResponsePrinted;
        }

        public void setDateResponsePrinted(Long dateResponsePrinted) {
            this.dateResponsePrinted = dateResponsePrinted;
        }

        public Long getDateResponseReceivable() {
            return dateResponseReceivable;
        }

        public void setDateResponseReceivable(Long dateResponseReceivable) {
            this.dateResponseReceivable = dateResponseReceivable;
        }

        public Boolean getIsResponseReceived() {
            return isResponseReceived;
        }

        public void setIsResponseReceived(Boolean received) {
            isResponseReceived = received;
        }

        public String getResponseAdditionalInfo() {
            return responseAdditionalInfo;
        }

        public void setResponseAdditionalInfo(String responseAdditionalInfo) {
            this.responseAdditionalInfo = responseAdditionalInfo;
        }
    }

}
