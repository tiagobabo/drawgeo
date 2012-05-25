class CreateWords < ActiveRecord::Migration
  def change
    create_table :words do |t|
      t.string :word
      t.integer :difficulty

      t.timestamps
    end
  end
end
