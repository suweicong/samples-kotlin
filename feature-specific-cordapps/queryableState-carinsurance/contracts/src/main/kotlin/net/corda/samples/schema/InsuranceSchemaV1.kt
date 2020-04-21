package net.corda.samples.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * The family of schemas for IOUState.
 */
object InsuranceSchema

/**
 * An IOUState schema.
 */
object InsuranceSchemaV1 : MappedSchema(
        schemaFamily = InsuranceSchema.javaClass,
        version = 1,
        mappedTypes = listOf(PersistentClaim::class.java, PersistentInsurance::class.java, PersistentVehicle::class.java)) {

    @Entity
    @Table(name = "CLAIM_DETAIL")
    class PersistentClaim(
            @Column(name = "claimNumber")
            var claimNumber: String,

            @Column(name = "claimDescription")
            var claimDescription: String,

            @Column(name = "claimAmount")
            var claimAmount: Int
    ) : PersistentState() {
        // Default constructor required by hibernate.
        constructor(): this("", "", 0)
    }

    @Entity
    @Table(name = "VEHICLE_DETAIL")
    class PersistentVehicle(
            @Column(name = "Id")
            val uuid:UUID,

            @Column(name = "registrationNumber")
            val registrationNumber: String,

            @Column(name = "chasisNumber")
            val chasisNumber: String,

            @Column(name = "make")
            val make: String,

            @Column(name = "model")
            val model: String,

            @Column(name = "variant")
            val variant: String,

            @Column(name = "color")
            val color: String,

            @Column(name = "fuelType")
            val fuelType: String
    ):PersistentState(){
        // Default constructor required by hibernate.
        constructor(registrationNumber: String, chasisNumber: String, make: String, model: String, variant: String, color: String, fuelType: String) : this(
                UUID.randomUUID(),registrationNumber, chasisNumber, make, model,variant,color,fuelType)
        constructor() : this(UUID.randomUUID(),"","","","","","","")
    }


    @Entity
    @Table(name = "INSURANCE_DETAIL")
    class PersistentInsurance(
            @Column(name = "policyNumber")
            val policyNumber: String,
            @Column(name = "insuredValue")
            val insuredValue:Long,
            @Column(name = "duration")
            val duration:Int,
            @Column(name = "premium")
            val premium: Int,
            @OneToOne(cascade = [CascadeType.PERSIST])
            @JoinColumns(JoinColumn(name = "id", referencedColumnName = "id"), JoinColumn(name = "registrationNumber", referencedColumnName = "registrationNumber"))
            val vehicle: PersistentVehicle?,
            @OneToMany(cascade = [CascadeType.PERSIST])
            @JoinColumns(JoinColumn(name = "output_index", referencedColumnName = "output_index"), JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id"))
            val claims: List<PersistentClaim>
    ):PersistentState(),Serializable{
        constructor(): this("", 0, 0, 0, PersistentVehicle(), listOf())
    }
}